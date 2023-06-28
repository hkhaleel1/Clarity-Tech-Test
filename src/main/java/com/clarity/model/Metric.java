package com.clarity.model;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Metric
{
    private Long id;
    private String system;
    private String name;
    private int date;
    @Setter
    private int value;
}