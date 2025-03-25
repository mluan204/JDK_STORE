package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.service.BillService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bill")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/daily-revenue")
    public Integer getDailyRevenue() {
        return billService.getDailyRevenue();
    }

    @GetMapping("daily-num-order")
    public Integer getNumberOfBills() {
        return billService.getNumberOfBills();
    }
}
