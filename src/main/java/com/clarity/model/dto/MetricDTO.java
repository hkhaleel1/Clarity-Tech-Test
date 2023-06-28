package com.clarity.model.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MetricDTO
{
    private String system;
    private String name;
    private Integer date;
    private Integer value;
}