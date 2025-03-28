package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.CustomerDTO;
import com.example.Backend_IE303.entity.Customer;
import com.example.Backend_IE303.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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

    public List<CustomerDTO> getAll(){
        return customerRepository.findAll().stream().map(CustomerDTO::new).toList();
    }

    public CustomerDTO createCustomer(Customer req){
        req.setCreated_at(new Timestamp(System.currentTimeMillis()));
        Customer c = customerRepository.save(req);
        return new CustomerDTO(c);
    }

    public CustomerDTO getCustomerById(Integer id){
        Customer c = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay id"));
        return new CustomerDTO(c);
    }

    public void deleteCustomerById(Integer id){
        customerRepository.deleteById(id);
    }
}
