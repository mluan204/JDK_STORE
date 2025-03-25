package com.example.Backend_IE303.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {
    private final BillService billService;
    private final BillDetailService billDetailService;
    private final CustomerService customerService;

    public StatisticsService(BillService billService, BillDetailService billDetailService, CustomerService customerService) {
        this.billService = billService;
        this.billDetailService = billDetailService;
        this.customerService = customerService;
    }

    public Map<String, Object> getDailyStatistics() {
        Map<String, Object> response = new HashMap<>();

        // Thống kê hôm nay
        Map<String, Object> todayStats = getStatsForDay("today");
        response.put("homnay", todayStats);

        // Thống kê hôm qua
        Map<String, Object> yesterdayStats = getStatsForDay("yesterday");
        response.put("homqua", yesterdayStats);

        return response;
    }

    private Map<String, Object> getStatsForDay(String dayIndicator) {
        Map<String, Object> stats = new HashMap<>();

        // Lấy thông tin từ các service
        Integer revenue = "today".equals(dayIndicator)
                ? billService.getDailyRevenue()
                : billService.getYesterdayRevenue();

        Integer numberOfBills = "today".equals(dayIndicator)
                ? billService.getNumberOfBills()
                : billService.getYesterdayNumberOfBills();

        Integer totalProductsSold = "today".equals(dayIndicator)
                ? billDetailService.getTotalProductsSold()
                : billDetailService.getYesterdayProductsSold();

        Integer newCustomers = "today".equals(dayIndicator)
                ? customerService.getNewCustomers()
                : customerService.getYesterdayNewCustomers();

        // Xây dựng thống kê theo ngày
        stats.put("doanhthu", revenue != null ? revenue : 0);
        stats.put("tongbill", numberOfBills != null ? numberOfBills : 0);
        stats.put("sanphamdaban", totalProductsSold != null ? totalProductsSold : 0);
        stats.put("khachhangmoi", newCustomers != null ? newCustomers : 0);

        return stats;
    }
}