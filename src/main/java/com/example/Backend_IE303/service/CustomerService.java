package com.example.Backend_IE303.service;

import com.example.Backend_IE303.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Integer getNewCustomers() {
        return customerRepository.getNewCustomers();
    }

    public Integer getYesterdayNewCustomers() {
        return customerRepository.getYesterdayNewCustomers(java.time.LocalDate.now().minusDays(1));
    }

}
