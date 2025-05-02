package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.SalesChartDTO;
import com.example.Backend_IE303.entity.Bill;
import com.example.Backend_IE303.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesDataService {

    @Autowired
    private BillRepository billRepository;

    public SalesChartDTO getSalesData(String type, LocalDateTime startDate, LocalDateTime endDate) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        List<Bill> bills = billRepository.findByDateRange(startTimestamp, endTimestamp);

        SalesChartDTO chartDTO = new SalesChartDTO();

        if (type.equals("HOURLY")) {
            chartDTO.setLabels(List.of("00:00", "06:00", "12:00", "18:00"));
            chartDTO.setData(processHourlyData(bills));
        } else if (type.equals("DAILY")) {
            // Tính số ngày trong tháng
            YearMonth yearMonth = YearMonth.from(startDate);
            int daysInMonth = yearMonth.lengthOfMonth();

            List<String> labels = new ArrayList<>();
            for (int i = 1; i <= daysInMonth; i++) {
                labels.add(String.valueOf(i));
            }
            chartDTO.setLabels(labels);
            chartDTO.setData(processDailyData(bills, daysInMonth));
        } else {
            chartDTO.setLabels(List.of("T2", "T3", "T4", "T5", "T6", "T7", "CN"));
            chartDTO.setData(processWeeklyData(bills));
        }

        return chartDTO;
    }

    private List<Double> processHourlyData(List<Bill> bills) {
        Map<Integer, Double> hourlyRevenue = bills.stream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getCreatedAt().toLocalDateTime().getHour(),
                        Collectors.summingDouble(
                                bill -> bill.getAfter_discount() != null ? bill.getAfter_discount() : 0)));

        List<Double> result = new ArrayList<>();
        for (int hour : List.of(0, 6, 12, 18)) {
            result.add(hourlyRevenue.getOrDefault(hour, 0.0));
        }
        return result;
    }

    private List<Double> processDailyData(List<Bill> bills, int daysInMonth) {
        Map<Integer, Double> dailyRevenue = bills.stream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getCreatedAt().toLocalDateTime().getDayOfMonth(),
                        Collectors.summingDouble(
                                bill -> bill.getAfter_discount() != null ? bill.getAfter_discount() : 0)));

        List<Double> result = new ArrayList<>();
        for (int day = 1; day <= daysInMonth; day++) {
            result.add(dailyRevenue.getOrDefault(day, 0.0));
        }
        return result;
    }

    private List<Double> processWeeklyData(List<Bill> bills) {
        Map<Integer, Double> weeklyRevenue = bills.stream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getCreatedAt().toLocalDateTime().getDayOfWeek().getValue(),
                        Collectors.summingDouble(
                                bill -> bill.getAfter_discount() != null ? bill.getAfter_discount() : 0)));

        List<Double> result = new ArrayList<>();
        for (int day = 1; day <= 7; day++) {
            result.add(weeklyRevenue.getOrDefault(day, 0.0));
        }
        return result;
    }
}