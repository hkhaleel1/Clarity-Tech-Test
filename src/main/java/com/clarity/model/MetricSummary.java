package com.clarity.model;


import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MetricSummary
{
    private String system;
    private String name;
    private int from;
    private int to;
    private int value;
}