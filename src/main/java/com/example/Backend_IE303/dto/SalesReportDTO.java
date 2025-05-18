package com.example.Backend_IE303.dto;

import lombok.Data;

@Data
public class SalesReportDTO {
    // Tổng
    private int totalTransactions;
    private int totalAmount;

    // Khách vãng lai
    private int guestTransactions;
    private int guestAmount;

    // Khách thân thiết
    private int loyalTransactions;
    private int loyalAmount;

    // Chi tiết
    private int totalCustomers;
    private int averagePerCustomer;
    private int discountedBillsAmount;
    private int averageDiscountAmount;
    private int refundAmount; // luôn 0
    private int serviceFeeAmount; // luôn 0
}