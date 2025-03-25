package com.example.Backend_IE303.service;

import com.example.Backend_IE303.repository.BillDetailRepository;
import org.springframework.stereotype.Service;

@Service
public class BillDetailService {
    private final BillDetailRepository billDetailRepository;

    public BillDetailService(BillDetailRepository billDetailRepository) {
        this.billDetailRepository = billDetailRepository;
    }

    public Integer getTotalProductsSold() {
        return billDetailRepository.getTotalProductsSold();
    }

    public Integer getYesterdayProductsSold() {
        return billDetailRepository.getYesterdayProductsSold(java.time.LocalDate.now().minusDays(1));
    }


}
