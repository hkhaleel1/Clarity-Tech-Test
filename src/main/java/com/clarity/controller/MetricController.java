package com.clarity.controller;

import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;
import com.clarity.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class MetricController extends ExceptionController
{
    @Autowired
    private MetricService metricService;

    @GetMapping("/metrics")
    public ResponseEntity<List<Metric>> getMetrics(@RequestParam String system,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) Integer from,
                                                   @RequestParam(required = false) Integer to)
    {
        List<Metric> metrics = metricService.getMetrics(system, name, from, to);
        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }

    @GetMapping("/metrics/{id}")
    public ResponseEntity<Metric> getMetric(@PathVariable Long id)
    {
        Metric metric = metricService.getMetricById(id);
        return new ResponseEntity<>(metric, HttpStatus.OK);
    }

    @PostMapping("/metrics")
    public ResponseEntity<Metric> createMetric(@RequestBody Metric metric)
    {
        Metric createdMetric = metricService.createMetric(metric);
        return new ResponseEntity<>(createdMetric, HttpStatus.CREATED);
    }

    @PutMapping("/metrics/{id}")
    public ResponseEntity<Metric> updateMetric(@PathVariable Long id,
                                               @RequestBody Metric metric)
    {
        Metric updatedMetric = metricService.updateMetric(id, metric);
        return new ResponseEntity<>(updatedMetric, HttpStatus.OK);
    }

    @GetMapping("/metricSummary")
    public ResponseEntity<MetricSummary> getMetricSummary(@RequestParam String system,
                                                          @RequestParam(required = false) String name,
                                                          @RequestParam(required = false) Integer from,
                                                          @RequestParam(required = false) Integer to)
    {
        MetricSummary metricSummary = metricService.getMetricSummary(system, name, from, to);
        return new ResponseEntity<>(metricSummary, HttpStatus.OK);
    }

}