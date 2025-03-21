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
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Timestamp created_at;
    int total_cost;
    String note;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    List<ReceiptDetail> receiptDetails = new ArrayList<>();
}
