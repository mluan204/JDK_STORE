package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.BillDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BillService {
    Integer getDailyRevenue();

    Integer getYesterdayRevenue();

    Integer getNumberOfBills();

    Integer getYesterdayNumberOfBills();

    Page<BillDTO> getAllBills(Pageable pageable, String keyword, String startDateStr, String endDateStr);

    BillDTO getBillById(Integer id);

    BillDTO createBill(BillDTO billDTO, int pointsToUse);

    BillDTO updateBill(Integer billId, BillDTO request, int pointsToUse);

    Boolean deleteBill(Integer id);

}
