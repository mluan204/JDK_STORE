package com.example.Backend_IE303.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantityDTO {
    private Integer productId;
    private Long totalQuantity;
}