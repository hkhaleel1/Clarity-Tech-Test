package com.clarity.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MetricSummary
{
    private String system;
    private String name;
    private int from;
    private int to;
    private int value;
}