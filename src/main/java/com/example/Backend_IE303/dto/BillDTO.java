package com.example.Backend_IE303.dto;

import com.example.Backend_IE303.entity.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BillDTO {
    private int id;
    private int total_cost;
    private int after_discount;
    private int customer_id;
    private int employee_id;
    private List<BillDetailDTO> billDetails;
    private Integer pointsToUse;
    private Boolean isDeleted;
    private Boolean is_error;

    public BillDTO(int id, int total_cost, int after_discount, int customer_id, int employee_id, Boolean isDeleted, Boolean isError, List<BillDetailDTO> billDetails) {
        this.id = id;
        this.total_cost = total_cost;
        this.after_discount = after_discount;
        this.customer_id = customer_id;
        this.employee_id = employee_id;
        this.isDeleted = isDeleted;
        this.is_error = isError;
        this.billDetails = billDetails;
    }

    public static BillDTO fromEntity(Bill bill) {
        List<BillDetailDTO> details = bill.getBillDetails().stream()
                .map(BillDetailDTO::fromEntity)
                .collect(Collectors.toList());
        return new BillDTO(
                bill.getId(),
                bill.getTotal_cost(),
                bill.getAfter_discount(),
                bill.getCustomer().getId(),
                bill.getEmployee().getId(),
                bill.getIsDeleted(),
                bill.getIs_error(),
                details
        );
    }
}
