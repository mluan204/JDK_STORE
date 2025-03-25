package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.service.BillDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bill-detail")
public class BillDetailController {
    private final BillDetailService billDetailService;

    public BillDetailController(BillDetailService billDetailService) {
        this.billDetailService = billDetailService;
    }

    @GetMapping("/daily-products-sold")
    public Integer getTotalProductsSold() {
        return billDetailService.getTotalProductsSold();
    }
}
