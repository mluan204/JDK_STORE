package com.example.Backend_IE303.entity;

import com.example.Backend_IE303.entity.EmbeddedId.ReceiptProId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReceiptDetail {
    @EmbeddedId
    ReceiptProId id;

    @ManyToOne
    @MapsId("receiptId")
    @JoinColumn(name = "receipt_id")
    Receipt receipt;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;

    String supplier;
    int quantity;
    int input_price;
    boolean isCheck;


    public int getInputPrice() {

        return input_price;
    }
}
