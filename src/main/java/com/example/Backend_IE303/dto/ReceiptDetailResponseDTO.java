package com.example.Backend_IE303.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class ReceiptDetailResponseDTO {
    private Integer id;
    private Timestamp created_at;
    private int total_cost;
    private String note;
    private String employee_name;
    private List<ReceiptDetailDTO> receipt_details;

    public ReceiptDetailResponseDTO() {

    }
}
