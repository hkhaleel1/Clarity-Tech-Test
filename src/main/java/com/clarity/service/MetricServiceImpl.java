package com.clarity.service;

import com.clarity.exception.EntityNotFoundException;
import com.clarity.model.Metric;
import com.clarity.model.MetricSummary;
import com.clarity.model.converter.MetricConverter;
import com.clarity.model.dto.MetricDTO;
import com.clarity.repository.MetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricServiceImpl implements MetricService
{
    @Autowired
    private MetricRepository repo;
    @Autowired
    private MetricConverter converter;

    @Override
    public List<Metric> getMetrics(final String system,
                                   final String name,
                                   final Integer from,
                                   final Integer to)
    {
        if (system == null)
            throw new IllegalArgumentException("System is required");
        return repo.getMetrics(system, name, from, to);
    }

    @Override
    public Metric getMetricById(final Long id) throws EntityNotFoundException
    {
        if (id == null)
            throw new IllegalArgumentException("Id is required");
        Metric metric = repo.getMetricById(id);
        if (metric == null)
            throw new EntityNotFoundException("No metric found with id: " + id);
        return metric;
    }

    @Override
    public Metric createMetric(final MetricDTO metric)
    {
        if(metric.getSystem() == null)
            throw new IllegalArgumentException("System is required: " + metric);
        if(metric.getName() == null)
            throw new IllegalArgumentException("Name is required: " + metric);
        return repo.createMetric(converter.toEntity(metric));
    }

    @Override
    public Metric updateMetric(final Long id,
                               final MetricDTO updatedMetric) throws EntityNotFoundException
    {
        Metric metric = getMetricById(id);
        if(updatedMetric.getSystem() == null)
            throw new IllegalArgumentException("System is required: " + updatedMetric);
        if(updatedMetric.getName() == null)
            throw new IllegalArgumentException("Name is required: " + updatedMetric);
        if(updatedMetric.getDate() == null)
            throw new IllegalArgumentException("Date is required: " + updatedMetric);

        if(!metric.getSystem().equals(updatedMetric.getSystem()))
            throw new IllegalArgumentException("Current and updated value for system do no match:"
                    + metric.getSystem() + " "
                    + updatedMetric.getSystem());

        if(!metric.getName().equals(updatedMetric.getName()))
            throw new IllegalArgumentException("Current and updated value for name do no match:"
                    + metric.getName() + " "
                    + updatedMetric.getName());
        int newValue = updatedMetric.getValue() == null ? metric.getValue() + 1 :  updatedMetric.getValue();
        metric.setValue(newValue);
        return repo.save(metric);
    }

    @Override
    public MetricSummary getMetricSummary(final String system,
                                          final String name,
                                          final Integer from,
                                          final Integer to)
    {
        if (system == null)
            throw new IllegalArgumentException("System is required");
        return repo.getMetricSummary(system, name, from, to);
    }
}