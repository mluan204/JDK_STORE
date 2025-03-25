package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.service.StatisticsService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/thongke")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("")
    public Map<String, Object> getDailyStats() {
        return statisticsService.getDailyStatistics();
    }
}
