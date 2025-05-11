package com.example.Backend_IE303.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ReceiptDetailDTO {
    private Integer productId;
    private String supplier;
    private int quantity;
    private int input_price;
    private boolean isCheck;

    public ReceiptDetailDTO() {}

    public ReceiptDetailDTO(Integer id, @NonNull String name, int quantity, int inputPrice) {
    }
    // Getters and setters
}
