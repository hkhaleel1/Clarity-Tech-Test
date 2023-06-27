package com.clarity.controller;

import com.clarity.controller.utils.ResponseMapper;
import com.clarity.exception.EntityNotFoundException;
import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;
import com.clarity.service.MetricService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetricController.class)
class MetricControllerTest
{
    private static final ResponseMapper<Metric> MAPPER = new ResponseMapper<>();
    @MockBean
    private MetricService metricService;
    @Autowired
    private MockMvc mockMvc;

    @Nested
    class TestGetMetrics
    {
        private static final Metric METRIC = new Metric(1L, "system", "name", 1, 0);
        @Test
        void returns200withValidList_whenValidRequest() throws Exception
        {
            // WITH
            List<Metric> metrics = Collections.singletonList(METRIC);
            when(metricService.getMetrics(anyString(), anyString(), anyInt(), anyInt())).thenReturn(metrics);
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .get("/metrics")
                    .param("system", "system")
                    .param("name", "name")
                    .param("from", "0")
                    .param("to", "1")
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isOk()).andReturn();
            List<Metric> response = MAPPER.deserializeList(mvcResult.getResponse().getContentAsString(), Metric.class);
            assertEquals(metrics, response);
        }

        @Test
        void returns400_whenSystemNotProvided() throws Exception
        {
            // WITH
            final String expectedMessage = "Required request parameter 'system' for method parameter type String is not present";
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .get("/metrics")
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isBadRequest()).andReturn();
            assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
        }
    }


    @Nested
    class TestGetMetric
    {

        private static final Metric METRIC = new Metric(1L, "system", "name", 1, 0);
        @Test
        void returns200withMetric_whenValidRequest() throws Exception
        {
            // WITH
            when(metricService.getMetricById(anyLong())).thenReturn(METRIC);
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .get("/metrics/" + METRIC.getId())
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isOk()).andReturn();
            Metric response = MAPPER.deserialize(mvcResult.getResponse().getContentAsString(), Metric.class);
            assertEquals(METRIC, response);
        }

        // Cannot Test for null so have tested for whitespace which is converted to null
        @Test
        void returns400_whenNoId() throws Exception
        {
            // WITH
            final String expectedMessage = "Required URI template variable 'id' for method parameter type Long is present but converted to null";
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .get("/metrics/ ")
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print());
            // THEN
            MvcResult mvcResult = action.andExpect(status().isBadRequest()).andReturn();
            assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
        }

        @Test
        void returns400_whenInvalidId() throws Exception
        {
            // WITH
            final String expectedMessage = "expected message";
            when(metricService.getMetricById(anyLong())).thenThrow(new IllegalArgumentException(expectedMessage));
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .get("/metrics/" + 1L)
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print());
            // THEN
            MvcResult mvcResult = action.andExpect(status().isBadRequest()).andReturn();
            assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
        }

        @Test
        void returns404_whenMetricNotFound() throws Exception
        {
            // WITH
            final String expectedMessage = "expected message";
            when(metricService.getMetricById(anyLong())).thenAnswer((e) -> {throw new EntityNotFoundException(expectedMessage);});
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .get("/metrics/" + 1L)
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isNotFound()).andReturn();
            assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
        }
    }

    @Nested
    class TestCreateMetric
    {
        private static final Metric NEW_METRIC = new Metric(1L, "system", "name", 1, 0);
        @Test
        void returns201_whenMetricCreated() throws Exception
        {
            // WITH
            when(metricService.createMetric(any(Metric.class))).thenReturn(NEW_METRIC);
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .post("/metrics")
                    .content(MAPPER.asJsonString(NEW_METRIC))
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isCreated()).andReturn();
            Metric response = MAPPER.deserialize(mvcResult.getResponse().getContentAsString(), Metric.class);
            assertEquals(NEW_METRIC, response);
        }

        @Test
        void returns400_whenSystemOrNameIsInvalid() throws Exception
        {
            // WITH
            final String expectedMessage = "expected message";
            when(metricService.createMetric(any(Metric.class))).thenAnswer((e) -> {throw new IllegalArgumentException(expectedMessage);});
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .post("/metrics")
                    .content(MAPPER.asJsonString(NEW_METRIC))
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isBadRequest()).andReturn();
            assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
        }
    }

    @Nested
    class TestUpdateMetric
    {
        private static final Metric UPDATED_METRIC = new Metric(1L, "system", "name", 1, 1);
        @Test
        void returns200_whenMetricCreated() throws Exception
        {
            // WITH
            when(metricService.updateMetric(anyLong(), any(Metric.class))).thenReturn(UPDATED_METRIC);
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .put("/metrics/" + UPDATED_METRIC.getId())
                    .content(MAPPER.asJsonString(UPDATED_METRIC))
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isOk()).andReturn();
            Metric response = MAPPER.deserialize(mvcResult.getResponse().getContentAsString(), Metric.class);
            assertEquals(UPDATED_METRIC, response);
        }


        @Test
        void returns400_whenSystemOrNameOrDateIsInvalid() throws Exception
        {
            // WITH
            final String expectedMessage = "expected message";
            when(metricService.updateMetric(anyLong(), any(Metric.class))).thenAnswer((e) -> {throw new IllegalArgumentException(expectedMessage);});
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .put("/metrics/" + UPDATED_METRIC.getId())
                    .content(MAPPER.asJsonString(UPDATED_METRIC))
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isBadRequest()).andReturn();
            assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
        }

        @Test
        void returns404_whenMetricNotFound() throws Exception
        {
            // WITH
            final String expectedMessage = "expected message";
            when(metricService.updateMetric(anyLong(), any(Metric.class))).thenAnswer((e) -> {throw new EntityNotFoundException(expectedMessage);});
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .put("/metrics/" + UPDATED_METRIC.getId())
                    .content(MAPPER.asJsonString(UPDATED_METRIC))
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isNotFound()).andReturn();
            assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
        }
    }

    @Nested
    class TestGetMetricSummary
    {

        private static final ResponseMapper<MetricSummary> SUMMARY_MAPPER = new ResponseMapper<>();
        private static final MetricSummary SUMMARY = new MetricSummary("system", "name", 1, 1, 0);
        @Test
        void returns200withSummary_whenValidRequest() throws Exception
        {
            // WITH
            when(metricService.getMetricSummary(anyString(), anyString(), anyInt(), anyInt())).thenReturn(SUMMARY);
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .get("/metricSummary")
                    .param("system", "system")
                    .param("name", "name")
                    .param("from", "0")
                    .param("to", "1")
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isOk()).andReturn();
            MetricSummary response = SUMMARY_MAPPER.deserialize(mvcResult.getResponse().getContentAsString(), MetricSummary.class);
            assertEquals(SUMMARY, response);
        }

        @Test
        void returns400_whenSystemNotProvided() throws Exception
        {
            // WITH
            final String expectedMessage = "Required request parameter 'system' for method parameter type String is not present";
            // WHEN
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                    .get("/metricSummary")
                    .contentType(MediaType.APPLICATION_JSON));
            // THEN
            MvcResult mvcResult = action.andExpect(status().isBadRequest()).andReturn();
            assertEquals(expectedMessage, mvcResult.getResponse().getContentAsString());
        }
    }
}