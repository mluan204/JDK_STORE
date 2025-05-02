package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.SalesChartDTO;
import com.example.Backend_IE303.service.SalesDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:5173")
public class SalesDataController {

    @Autowired
    private SalesDataService salesDataService;

    @GetMapping("/chart")
    public SalesChartDTO getSalesChart(
            @RequestParam String type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        SalesChartDTO chartData = salesDataService.getSalesData(type, startDate, endDate);
        return chartData;
    }
}