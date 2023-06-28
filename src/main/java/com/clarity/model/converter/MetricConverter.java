package com.clarity.model.converter;

import com.clarity.model.Metric;
import com.clarity.model.dto.MetricDTO;
import org.springframework.stereotype.Component;

@Component
public class MetricConverter implements Converter<Metric, MetricDTO>
{
    @Override
    public MetricDTO toDTO(Metric metric) {
        return MetricDTO.builder()
                .system(metric.getSystem())
                .name(metric.getName())
                .date(metric.getDate())
                .value(metric.getValue())
                .build();
    }

    @Override
    public Metric toEntity(MetricDTO metricDTO)
    {
        int date = metricDTO.getDate() == null ? 1 : metricDTO.getDate();
        int value = metricDTO.getValue() == null ? 1 : metricDTO.getValue();
        return Metric.builder()
                .id(null)
                .system(metricDTO.getSystem())
                .name(metricDTO.getName())
                .date(date)
                .value(value)
                .build();
    }
}