package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.ProductQuantityDTO;
import com.example.Backend_IE303.service.BillDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/product-quantities")
    public List<ProductQuantityDTO> getTotalQuantityByProduct() {
        return billDetailService.getTotalQuantityByProduct();
    }
}
