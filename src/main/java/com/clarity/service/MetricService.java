package com.clarity.service;

import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;

import java.util.List;

public interface MetricService {
    List<Metric> getMetrics(String system, String name, int from, int to);

    Metric getMetricById(Long id);

    Metric createMetric(Metric metric);

    Metric updateMetric(Long id, Metric metric);

    MetricSummary getMetricSummary(String system, String name, int from, int to);
}