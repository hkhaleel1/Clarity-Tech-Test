package com.clarity.service;

import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricServiceImpl implements MetricService
{
    @Override
    public List<Metric> getMetrics(String system, String name, int from, int to) {
        return null;
    }

    @Override
    public Metric getMetricById(Long id) {
        return null;
    }

    @Override
    public Metric createMetric(Metric metric) {
        return null;
    }

    @Override
    public Metric updateMetric(Long id, Metric metric) {
        return null;
    }

    @Override
    public MetricSummary getMetricSummary(String system, String name, int from, int to) {
        return null;
    }
}
