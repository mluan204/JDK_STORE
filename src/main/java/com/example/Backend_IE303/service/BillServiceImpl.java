package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.BillDTO;
import com.example.Backend_IE303.entity.Bill;
import com.example.Backend_IE303.repository.BillRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
public class BillService {
    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public Integer getDailyRevenue() {
        return billRepository.getDailyRevenue();
    }

    public Integer getYesterdayRevenue() {
        return billRepository.getYesterdayRevenue(java.time.LocalDate.now().minusDays(1));
    }

    public Integer getNumberOfBills() {
        return billRepository.getNumberOfBills();}

    public Integer getYesterdayNumberOfBills() {
        return billRepository.getYesterdayNumberOfBills(java.time.LocalDate.now().minusDays(1));
    }

    public List<BillDTO> getAllBills() {
        return billRepository.findAll().stream()
                .map(BillDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
