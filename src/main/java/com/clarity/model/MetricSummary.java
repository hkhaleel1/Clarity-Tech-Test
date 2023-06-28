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
    private Integer from;
    private Integer to;
    private int value;
}