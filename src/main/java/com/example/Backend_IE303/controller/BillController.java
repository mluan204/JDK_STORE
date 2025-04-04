package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.BillDTO;
import com.example.Backend_IE303.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillController {
    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping("/daily-revenue")
    public Integer getDailyRevenue() {
        return billService.getDailyRevenue();
    }

    @GetMapping("/daily-num-order")
    public Integer getNumberOfBills() {
        return billService.getNumberOfBills();
    }

    @GetMapping("/get-all-bills")
    public ResponseEntity<List<BillDTO>> getAllBills() {
        return ResponseEntity.ok(billService.getAllBills());
    }



    @GetMapping("/paged")
    public ResponseEntity<Page<BillDTO>> searchBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(billService.getAllBills(pageable, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Integer id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }


    @PostMapping("/create-bill")
    public ResponseEntity<BillDTO> createBill(
            @RequestBody BillDTO request) {
        return ResponseEntity.ok(billService.createBill(request, request.getPointsToUse()));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable Integer id) {
        boolean isDeleted = billService.deleteBill(id);
        if (isDeleted) {
            return ResponseEntity.ok("Success: Bill has been deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Fail: Bill not found or could not be deleted.");
        }
    }

    @PutMapping("/delete_error/{id}")
    public ResponseEntity<String> deleteBillError(@PathVariable Integer id) {
        boolean isDeleted = billService.deleteErrorBill(id);
        if (isDeleted) {
            return ResponseEntity.ok("Success: Bill has been deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Fail: Bill not found or could not be deleted.");
        }
    }

}
