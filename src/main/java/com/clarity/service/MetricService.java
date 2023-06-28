package com.clarity.service;

import com.clarity.exception.EntityNotFoundException;
import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;
import com.clarity.model.dto.MetricDTO;

import java.util.List;

public interface MetricService
{
    List<Metric> getMetrics(String system, String name, Integer from, Integer to);

    Metric getMetricById(Long id) throws EntityNotFoundException;

    Metric createMetric(MetricDTO metric);

    Metric updateMetric(Long id, MetricDTO updatedMetric) throws EntityNotFoundException;

    MetricSummary getMetricSummary(String system, String name, Integer from, Integer to);
}