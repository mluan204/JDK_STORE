package com.example.Backend_IE303.dto;

import lombok.Data;
import java.util.List;

@Data
public class SalesChartDTO {
    private List<String> labels;
    private List<Double> data;
}