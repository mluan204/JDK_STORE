package com.example.Backend_IE303.dto;

import com.example.Backend_IE303.entity.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BillDTO {
    private int id;
    private int total_cost;
    private int after_discount;
    private CustomerDTO customer;
    private EmployeeDTO employee;
    private List<BillDetailDTO> billDetails;
    private Integer pointsToUse;
    private Boolean isDeleted;
    private Boolean is_error;
    private Timestamp createdAt;
    private Integer totalQuantity;
    private String notes;

    public BillDTO(int id, int total_cost, int after_discount, CustomerDTO customer, EmployeeDTO employee, Boolean isDeleted, Boolean isError, List<BillDetailDTO> billDetails, Timestamp createdAt, Integer totalQuantity, String notes) {
        this.id = id;
        this.total_cost = total_cost;
        this.after_discount = after_discount;
        this.customer = customer;
        this.employee = employee;
        this.isDeleted = isDeleted;
        this.is_error = isError;
        this.billDetails = billDetails;
        this.createdAt = createdAt;
        this.totalQuantity = totalQuantity;
        this.notes = notes;
    }

    public static BillDTO fromEntity(Bill bill) {
        List<BillDetailDTO> details = bill.getBillDetails().stream()
                .map(BillDetailDTO::fromEntity)
                .collect(Collectors.toList());
        CustomerDTO customer = new CustomerDTO();
        customer.setId(bill.getCustomer().getId());
        customer.setName(bill.getCustomer().getName());
        customer.setPhone_number(bill.getCustomer().getPhone_number());

        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(bill.getEmployee().getId());
        employee.setName(bill.getEmployee().getName());
        employee.setPhone_number(bill.getEmployee().getPhone_number());

        return new BillDTO(
                bill.getId(),
                bill.getTotal_cost(),
                bill.getAfter_discount(),
                customer,
                employee,
                bill.getIsDeleted(),
                bill.getIs_error(),
                details,
                bill.getCreatedAt(),
                bill.getTotalQuantity(),
                bill.getNotes()

        );
    }
}
