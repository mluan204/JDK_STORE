package com.example.Backend_IE303.entity;

import com.example.Backend_IE303.entity.EmbeddedId.BillProId;
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
public class BillDetail {
    @EmbeddedId
    BillProId id;

    @ManyToOne
    @MapsId("billId")
    @JoinColumn(name = "bill_id")
    Bill bill;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    int price;
    int after_discount;
    int quantity;
}
