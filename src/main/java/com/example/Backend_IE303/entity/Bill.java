package com.example.Backend_IE303.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @Column(name = "created_at")
    Timestamp createdAt;
    int total_cost;

    @Column(nullable = true)
    private int totalQuantity = 0;

    @Column(name="is_deleted" ,nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean isDeleted = false;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean is_error = false;

    @Column(nullable = true)
    Integer after_discount;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    List<BillDetail> billDetails = new ArrayList<>();

    @PrePersist
    void prePersist() {
        if (isDeleted == null) {
            isDeleted = false; // Đảm bảo giá trị không bị null khi lưu vào DB
        }
        if (is_error == null) {
            is_error = false; // Đảm bảo giá trị không bị null khi lưu vào DB
        }
    }
}
