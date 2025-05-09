package com.example.Backend_IE303.dto;

import com.example.Backend_IE303.entity.Bill;
import com.example.Backend_IE303.entity.Employee;
import com.example.Backend_IE303.entity.EmployeeShift;
import com.example.Backend_IE303.entity.Receipt;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDTO {
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

    List<Integer> employeeShifts = new ArrayList<>();
    List<Integer> bills = new ArrayList<>();
    List<Integer> receipts = new ArrayList<>();

    public EmployeeDTO(Employee e){
        id = e.getId();
        created_at = e.getCreated_at();
        name = e.getName();
        gender = e.isGender();
        birthday = e.getBirthday();
        image = e.getImage();
        salary = e.getSalary();
        phone_number = e.getPhone_number();
        email = e.getEmail();
        address = e.getAddress();
        position = e.getPosition();

        employeeShifts = e.getEmployeeShifts().stream().map(EmployeeShift::getId).toList();
        bills = e.getBills().stream().map(Bill::getId).toList();
        receipts = e.getReceipts().stream().map(Receipt::getId).toList();
    }


    public Employee mappingToEmployee(){
        Employee e = new Employee();
        e.setId(id);
        e.setCreated_at(created_at);
        e.setName(name);
        e.setGender(gender);
        e.setBirthday(birthday);
        e.setImage(image);
        e.setSalary(salary);
        e.setPhone_number(phone_number);
        e.setEmail(email);
        e.setAddress(address);
        e.setPosition(position);

        List<EmployeeShift> employeeShifts1 = employeeShifts.stream().map(emId -> {
            EmployeeShift em = new EmployeeShift();
            em.setId(emId);
            return em;}
        ).toList();

        List<Bill> billList = bills.stream().map(emId -> {
            Bill em = new Bill();
            em.setId(emId);
            return em;}
        ).toList();

        List<Receipt> receiptList = receipts.stream().map(emId -> {
            Receipt em = new Receipt();
            em.setId(emId);
            return em;}
        ).toList();

        return e;
    }
}
