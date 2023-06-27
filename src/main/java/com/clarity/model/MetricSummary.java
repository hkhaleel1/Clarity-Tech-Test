package com.clarity.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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