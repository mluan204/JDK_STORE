package com.example.Backend_IE303.entity;

import com.example.Backend_IE303.entity.EmbeddedId.ComboProductId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "combo_product")
public class ComboProduct {
    @EmbeddedId
    ComboProductId id;

    @ManyToOne
    @MapsId("comboId")
    @JoinColumn(name = "combo_id")
    Combo combo;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    int price;
    int quantity;
}
