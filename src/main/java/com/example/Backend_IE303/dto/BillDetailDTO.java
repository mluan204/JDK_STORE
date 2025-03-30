package com.example.Backend_IE303.dto;

import com.example.Backend_IE303.entity.BillDetail;
import lombok.Data;

@Data
public class BillDetailDTO {
    private int productId;
    private int price;
    private Integer afterDiscount;
    private int quantity;

    public BillDetailDTO(int productId, int price, Integer afterDiscount, int quantity) {
        this.productId = productId;
        this.price = price;
        this.afterDiscount = afterDiscount;
        this.quantity = quantity;
    }

    public static BillDetailDTO fromEntity(BillDetail billDetail) {
        return new BillDetailDTO(
                billDetail.getProduct().getId(),
                billDetail.getPrice(),
                billDetail.getAfter_discount(),
                billDetail.getQuantity()
        );
    }
}
