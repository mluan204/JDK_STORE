package com.example.Backend_IE303.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ReceiptDTO {
    public ReceiptDetailDTO[] receiptDetails;
    private Timestamp created_at;
    private int total_cost;
    private String note;
    private Integer employeeId;

    public ReceiptDTO() {}

    public ReceiptDTO(Integer id, Timestamp createdAt, int totalCost, String note, String note1) {
    }


    // Getters and setters
}

