package com.clarity.repository;

import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;

import java.util.List;

public interface MetricRepository
{
    List<Metric> getMetrics(String system, String name, Integer from, Integer to);

    Metric getMetricById(Long id);

    Metric createMetric(Metric metric);

    Metric persist(Metric metric);

    MetricSummary getMetricSummary(String system, String name, Integer from, Integer to);
}