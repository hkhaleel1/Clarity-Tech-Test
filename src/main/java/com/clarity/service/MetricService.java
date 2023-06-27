package com.clarity.service;

import com.clarity.model.Metric;

import java.util.List;

public interface MetricService {
    List<Metric> getMetrics(String system, String name, int from, int to);

    Metric getMetricById(int id);

    Metric createMetric(Metric metric);

    Metric updateMetric(int id, Metric metric);
}