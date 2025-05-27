package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.ReceiptDTO;
import com.example.Backend_IE303.dto.ReceiptDetailResponseDTO;
import com.example.Backend_IE303.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @GetMapping("/all")
    public ResponseEntity<List<ReceiptDTO>> getAllReceipts() {
        List<ReceiptDTO> receipts = receiptService.getAllReceipts();
        return ResponseEntity.ok(receipts);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<ReceiptDetailResponseDTO>> getReceiptsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ReceiptDetailResponseDTO> receipts = receiptService.getReceiptsPaginated(page, size);
        return ResponseEntity.ok(receipts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReceiptById(@PathVariable Integer id) {
        ReceiptDTO receipt = receiptService.getReceiptById(id);
        if (receipt == null) {
            return ResponseEntity.status(404).body("Receipt not found");
        }
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getReceiptDetails(@PathVariable Integer id) {
        ReceiptDetailResponseDTO receiptDetails = receiptService.getReceiptDetailsById(id);
        if (receiptDetails == null) {
            return ResponseEntity.status(404).body("Receipt not found");
        }
        return ResponseEntity.ok(receiptDetails);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReceipt(@RequestBody ReceiptDTO dto) {
        receiptService.addReceipt(dto);
        return ResponseEntity.ok("Thêm phiếu nhập thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReceipt(@PathVariable Integer id) {
        boolean deleted = receiptService.deleteReceiptById(id);

        if (deleted) {
            return ResponseEntity.ok("Receipt with id " + id + " was deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Receipt with id " + id + " not found.");
        }
    }


    @GetMapping("/search")
    public ResponseEntity<Page<ReceiptDetailResponseDTO>> getFilteredReceipts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String fromDate,     // dạng: 01-01-2025
            @RequestParam(required = false) String toDate,       // dạng: 31-01-2025
            @RequestParam(required = false) String employeeName
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate from = null;
        LocalDate to = null;

        try {
            if (fromDate != null && !fromDate.isEmpty()) {
                from = LocalDate.parse(fromDate, formatter);
            }
            if (toDate != null && !toDate.isEmpty()) {
                to = LocalDate.parse(toDate, formatter);
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Page.empty()); // hoặc trả lỗi cụ thể
        }

        Page<ReceiptDetailResponseDTO> result = receiptService.getReceiptsPaginatedFiltered(page, size, from, to, employeeName);
        return ResponseEntity.ok(result);
    }


}
