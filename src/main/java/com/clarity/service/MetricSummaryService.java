package com.clarity.service;

import com.clarity.model.MetricSummary;

public interface MetricSummaryService {
    MetricSummary getMetricSummary(String system, String name, int from, int to);
}
