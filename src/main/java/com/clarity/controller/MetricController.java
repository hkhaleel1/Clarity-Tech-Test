package com.clarity.controller;

import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;
import com.clarity.service.MetricService;
import com.clarity.service.MetricSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/metrics")
public class MetricController extends ExceptionController
{
    @Autowired
    private MetricService metricService;
    @Autowired
    private MetricSummaryService metricSummaryService;

    @GetMapping
    public ResponseEntity<List<Metric>> getMetrics(@RequestParam String system,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) int from,
                                                   @RequestParam(required = false) int to)
    {
        List<Metric> metrics = metricService.getMetrics(system, name, from, to);
        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metric> getMetric(@PathVariable int id)
    {
        Metric metric = metricService.getMetricById(id);
        return new ResponseEntity<>(metric, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Metric> createMetric(@RequestBody Metric metric)
    {
        Metric createdMetric = metricService.createMetric(metric);
        return new ResponseEntity<>(createdMetric, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Metric> updateMetric(@PathVariable int id,
                                               @RequestBody Metric metric) {
        Metric updatedMetric = metricService.updateMetric(id, metric);
        return new ResponseEntity<>(updatedMetric, HttpStatus.OK);
    }

    @GetMapping("/metricSummary")
    public ResponseEntity<MetricSummary> getMetricSummary(@RequestParam String system,
                                                          @RequestParam(required = false) String name,
                                                          @RequestParam(required = false) int from,
                                                          @RequestParam(required = false) int to
    ) {
        MetricSummary metricSummary = metricSummaryService.getMetricSummary(system, name, from, to);
        return new ResponseEntity<>(metricSummary, HttpStatus.OK);
    }

}