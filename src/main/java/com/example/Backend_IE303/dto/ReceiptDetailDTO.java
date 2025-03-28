package com.example.Backend_IE303.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceiptDetailDTO {
    private Integer product_id;
    private String product_name;
    private Integer quantity;
    private Integer price;
}
