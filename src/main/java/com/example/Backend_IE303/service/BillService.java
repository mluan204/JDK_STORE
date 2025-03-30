package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.BillDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BillService {
    Integer getDailyRevenue();

    Integer getYesterdayRevenue();

    Integer getNumberOfBills();

    Integer getYesterdayNumberOfBills();

    List<BillDTO> getAllBills();

    Page<BillDTO> getAllBills(Pageable pageable);

    BillDTO getBillById(Integer id);

    BillDTO createBill(BillDTO billDTO, int pointsToUse);

    Boolean deleteBill(Integer id);

    Boolean deleteErrorBill(Integer id);
}
