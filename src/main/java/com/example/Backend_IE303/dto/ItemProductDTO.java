package com.example.Backend_IE303.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemProductDTO {
    private Integer id;
    private Integer price;
    private Integer quantity;
}
