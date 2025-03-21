package com.example.Backend_IE303.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Timestamp created_at;
    String name;
    boolean gender;
    Date birthday;
    String image;
    int salary;
    String phone_number;
    String email;
    String address;
    String position;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<EmployeeShift> employeeShifts = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<Bill> bills = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<Receipt> receipts = new ArrayList<>();
}
