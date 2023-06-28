package com.clarity.service;

import com.clarity.exception.EntityNotFoundException;
import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;
import com.clarity.model.converter.MetricConverter;
import com.clarity.model.dto.MetricDTO;
import com.clarity.repository.MetricRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest
{
    private static final Metric METRIC = new Metric(1L, "system", "name", 12, 12);

    @Mock
    private MetricRepository repo;
    @Mock
    private MetricConverter converter;
    @InjectMocks
    private MetricServiceImpl service;

    @Nested
    class TestGetMetrics
    {
        @Test
        void returnsListOfMetrics_whenValidParameters()
        {
            // WITH
            final List<Metric> metrics = Collections.singletonList(METRIC);
            when(repo.getMetrics(anyString(), any(), any(), any())).thenReturn(metrics);
            // WHEN
            final List<Metric> results = service.getMetrics(METRIC.getSystem(), null, null, null);
            // THEN
            assertEquals(1, results.size());
            assertEquals(METRIC, results.get(0));
        }

        @Test
        void throwsException_whenSystemIsNull()
        {
            // WITH
            final String expectedMessage = "System is required";
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.getMetrics(null, null, null, null));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Nested
    class TestGetMetricById
    {
        @Test
        void returnsMetrics_whenValidId() throws EntityNotFoundException
        {
            // WITH
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(METRIC);
            // WHEN
            final Metric result = service.getMetricById(METRIC.getId());
            // THEN
            assertEquals(METRIC, result);
        }

        @Test
        void throwsException_whenIdIsNull()
        {
            // WITH
            final String expectedMessage = "Id is required";
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.getMetricById(null));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        void throwsEntityNotFoundException_whenNoMetricFound()
        {
            // WITH
            final String expectedMessage = "No metric found with id: " + METRIC.getId();
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(null);
            // WHEN
            final EntityNotFoundException exception
                    = assertThrows(EntityNotFoundException.class,
                    () -> service.getMetricById(METRIC.getId()));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Nested
    class TestCreateMetric
    {
        @Test
        void createsMetric_whenValidMetric()
        {
            // WITH
            final MetricDTO DTO = MetricDTO.builder()
                    .system(METRIC.getSystem())
                    .name(METRIC.getName())
                    .date(METRIC.getDate())
                    .value(METRIC.getValue())
                    .build();
            when(converter.toEntity(any(MetricDTO.class))).thenReturn(METRIC);
            when(repo.createMetric(any(Metric.class))).thenReturn(METRIC);
            // WHEN
            final Metric result = service.createMetric(DTO);
            // THEN
            assertEquals(METRIC, result);
        }

        @Test
        void throwsException_whenSystemIsNull()
        {
            // WITH
            final MetricDTO DTO = MetricDTO.builder().build();
            final String expectedMessage = "System is required: " + DTO;
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.createMetric(DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        void throwsException_whenNameIsNull()
        {
            // WITH
            final MetricDTO DTO = MetricDTO.builder()
                    .system(METRIC.getSystem())
                    .build();
            final String expectedMessage = "Name is required: " + DTO;
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.createMetric(DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Nested
    class TestUpdateMetric
    {
        @Test
        void updatesMetric_whenValidIdAndMetric() throws EntityNotFoundException {
            // WITH
            final int newVal = 5;
            final MetricDTO DTO = MetricDTO.builder()
                    .system(METRIC.getSystem())
                    .name(METRIC.getName())
                    .date(METRIC.getDate())
                    .value(newVal)
                    .build();
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(METRIC);
            when(repo.persist(any(Metric.class))).then(returnsFirstArg());
            // WHEN
            final Metric result = service.updateMetric(METRIC.getId(), DTO);
            // THEN
            assertEquals(METRIC.getId(), result.getId());
            assertEquals(METRIC.getSystem(), result.getSystem());
            assertEquals(METRIC.getName(), result.getName());
            assertEquals(METRIC.getDate(), result.getDate());
            assertEquals(newVal, result.getValue());
        }
        @Test
        void incrementsValueBy1_whenValidIdAndMetricButValueIsNull() throws EntityNotFoundException {
            // WITH
            final int expectedVal = METRIC.getValue() + 1;
            final MetricDTO DTO = MetricDTO.builder()
                    .system(METRIC.getSystem())
                    .name(METRIC.getName())
                    .date(METRIC.getDate())
                    .value(null)
                    .build();
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(METRIC);
            when(repo.persist(any(Metric.class))).then(returnsFirstArg());
            // WHEN
            final Metric result = service.updateMetric(METRIC.getId(), DTO);
            // THEN
            assertEquals(METRIC.getId(), result.getId());
            assertEquals(METRIC.getSystem(), result.getSystem());
            assertEquals(METRIC.getName(), result.getName());
            assertEquals(METRIC.getDate(), result.getDate());
            assertEquals(expectedVal, result.getValue());
        }

        @Test
        void throwsException_whenIdIsNull()
        {
            // WITH
            final String expectedMessage = "Id is required";
            final MetricDTO DTO = MetricDTO.builder().build();
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.updateMetric(null, DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        void throwsEntityNotFoundException_whenNoMetricFound()
        {
            // WITH
            final String expectedMessage = "No metric found with id: " + METRIC.getId();
            final MetricDTO DTO = MetricDTO.builder().build();
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(null);
            // WHEN
            final EntityNotFoundException exception
                    = assertThrows(EntityNotFoundException.class,
                    () -> service.updateMetric(METRIC.getId(), DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        void throwsException_whenSystemIsNull()
        {
            // WITH
            final MetricDTO DTO = MetricDTO.builder().build();
            final String expectedMessage = "System is required: " + DTO;
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(METRIC);
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.updateMetric(METRIC.getId(), DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        void throwsException_whenNameIsNull()
        {
            // WITH
            final MetricDTO DTO = MetricDTO.builder()
                    .system(METRIC.getSystem()).build();
            final String expectedMessage = "Name is required: " + DTO;
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(METRIC);
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.updateMetric(METRIC.getId(), DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        void throwsException_whenDateIsNull()
        {
            // WITH
            final MetricDTO DTO = MetricDTO.builder()
                    .system(METRIC.getSystem())
                    .name(METRIC.getName()).build();
            final String expectedMessage = "Date is required: " + DTO;
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(METRIC);
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.updateMetric(METRIC.getId(), DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        void throwsException_whenSystemDoesNotMatch()
        {
            // WITH
            final MetricDTO DTO = MetricDTO.builder()
                    .system("Another system")
                    .name(METRIC.getName())
                    .date(METRIC.getDate()).build();
            final String expectedMessage
                    = "Current and updated value for system do no match:" + METRIC.getSystem()
                    + " " + DTO.getSystem();
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(METRIC);
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.updateMetric(METRIC.getId(), DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }

        @Test
        void throwsException_whenNameDoesNotMatch()
        {
            // WITH
            final MetricDTO DTO = MetricDTO.builder()
                    .system(METRIC.getSystem())
                    .name("Another name")
                    .date(METRIC.getDate()).build();
            final String expectedMessage
                    = "Current and updated value for name do no match:" + METRIC.getName()
                    + " " + DTO.getName();
            when(repo.getMetricById(eq(METRIC.getId()))).thenReturn(METRIC);
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.updateMetric(METRIC.getId(), DTO));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }
    }

    @Nested
    class TestGetMetricSummary
    {
        @Test
        void returnsMetricSummary_whenValidParameters()
        {
            // WITH
            final MetricSummary summary = MetricSummary.builder().system(METRIC.getSystem()).build();
            when(repo.getMetricSummary(anyString(), any(), any(), any())).thenReturn(summary);
            // WHEN
            MetricSummary result = service.getMetricSummary(METRIC.getSystem(), null, null, null);
            // THEN
            assertEquals(summary, result);
        }

        @Test
        void throwsException_whenSystemIsNull()
        {
            // WITH
            final String expectedMessage = "System is required";
            // WHEN
            final IllegalArgumentException exception
                    = assertThrows(IllegalArgumentException.class,
                    () -> service.getMetricSummary(null, null, null, null));
            // THEN
            assertEquals(expectedMessage, exception.getMessage());
        }
    }
}