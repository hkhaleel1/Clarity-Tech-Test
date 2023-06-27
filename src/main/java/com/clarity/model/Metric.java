package com.clarity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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