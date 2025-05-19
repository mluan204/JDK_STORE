package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.ProductQuantityDTO;
import com.example.Backend_IE303.repository.BillDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<ProductQuantityDTO> getTotalQuantityByProduct() {
        return billDetailRepository.getTotalQuantityByProduct().stream()
                .map(result -> new ProductQuantityDTO(
                        (Integer) result[0],
                        ((Number) result[1]).longValue()))
                .filter(dto -> dto.getTotalQuantity() > 0)
                .collect(Collectors.toList());
    }
}
