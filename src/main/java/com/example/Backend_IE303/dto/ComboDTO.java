package com.example.Backend_IE303.dto;

import com.example.Backend_IE303.entity.Combo;
import com.example.Backend_IE303.entity.ComboProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboDTO {
    private Integer id;
    private Timestamp createdAt;
    private Timestamp timeEnd;
    private List<ComboProductDTO> comboProducts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComboProductDTO {
        private Integer productId;
        private Integer price;
        private Integer quantity;
    }

    public static ComboDTO fromEntity(Combo combo) {
        List<ComboProductDTO> comboProductDTOs = combo.getComboProducts().stream()
                .map(cp -> new ComboProductDTO(
                        cp.getProduct().getId(),
                        cp.getPrice(),
                        cp.getQuantity()))
                .collect(Collectors.toList());

        return new ComboDTO(
                combo.getId(),
                combo.getCreatedAt(),
                combo.getTimeEnd(),
                comboProductDTOs);
    }
}
