package com.clarity.repository;

import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class MetricRepositoryInMemoryImpl implements MetricRepository
{
    private final Map<Long, Metric> metricMap = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public List<Metric> getMetrics(final String system,
                                   final String name,
                                   final Integer from,
                                   final Integer to)
    {
        final Predicate<Metric> filter = metric -> metric.getSystem().equals(system)
                && (name == null || metric.getName().equals(name))
                && (from == null || metric.getDate() >= from)
                && (to == null || metric.getDate() <= to);
        return metricMap.values().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public Metric getMetricById(final Long id)
    {
        return metricMap.get(id);
    }

    @Override
    public Metric createMetric(final Metric metric)
    {
        final Long newId = getNextId();
        metric.setId(newId);
        metricMap.put(newId, metric);
        return metric;
    }

    @Override
    public Metric persist(final Metric metric)
    {
        if (metric.getId() == null) {
            return createMetric(metric);
        } else {
            metricMap.put(metric.getId(), metric);
            return metric;
        }
    }

    @Override
    public MetricSummary getMetricSummary(final String system,
                                          final String name,
                                          final Integer from,
                                          final Integer to) {
        final List<Metric> metrics = getMetrics(system, name, from, to);
        final int totalValue = metrics.stream()
                .mapToInt(Metric::getValue)
                .sum();
        return MetricSummary.builder()
                .system(system)
                .name(name)
                .from(from)
                .to(to)
                .value(totalValue).build();
    }

    private Long getNextId() {
        return nextId++;
    }
}