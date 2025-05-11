package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.ReceiptDTO;
import com.example.Backend_IE303.dto.ReceiptDetailResponseDTO;
import com.example.Backend_IE303.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<ReceiptDTO>> getReceiptsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ReceiptDTO> receipts = receiptService.getReceiptsPaginated(page, size);
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
}
