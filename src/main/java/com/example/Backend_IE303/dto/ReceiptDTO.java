package com.example.Backend_IE303.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class ReceiptDTO {
    private Integer id;
    private Timestamp created_at;
    private int total_cost;
    private String note;
    private String employee_name;
}
