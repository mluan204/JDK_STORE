package com.example.Backend_IE303.dto;

import com.example.Backend_IE303.entity.Bill;
import com.example.Backend_IE303.entity.Customer;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomerDTO {
    Integer id;
    Timestamp created_at;
    String name;
    boolean gender;
    int score;
    String phone_number;

    List<Integer> bills = new ArrayList<>();

    public CustomerDTO(Customer customer){
        id = customer.getId();
        created_at = customer.getCreated_at();
        name = customer.getName();
        gender = customer.isGender();
        score = customer.getScore();
        phone_number = customer.getPhone_number();

        bills = customer.getBills().stream().map(Bill::getId).toList();
    }



}
