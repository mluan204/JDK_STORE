package com.example.Backend_IE303.dto;

import com.example.Backend_IE303.entity.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    Integer id;
    String name;
    String description;
    String image;
    String suppliers;
    Integer quantityAvailable;
    Timestamp dateExpired;
    Integer salePrice;
    Integer inputPrice;
    Integer price;

    // Tránh vòng lặp bằng cách chỉ lấy ID và Name thay vì toàn bộ Category
    Integer categoryId;
    String categoryName;

    // Chuyển từ `Product` sang `ProductDTO`
    public static ProductDTO fromEntity(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getImage(),
                product.getSuppliers(),
                product.getQuantity_available(),
                product.getDate_expired(),
                product.getSale_price(),
                product.getInput_price(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }

}
