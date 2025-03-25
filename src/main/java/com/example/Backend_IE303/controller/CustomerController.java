package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    public final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/daily-new-customers")
    public Integer getNewCustomers() {
        return customerService.getNewCustomers();
    }
}
