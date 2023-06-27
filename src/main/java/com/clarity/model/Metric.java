package com.clarity.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Metric
{
    private Long id;
    private String system;
    private String name;
    private int date;
    private int value;
}