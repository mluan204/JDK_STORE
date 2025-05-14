package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.CustomerDTO;
import com.example.Backend_IE303.entity.Customer;
import com.example.Backend_IE303.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    public final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomer(){
        return ResponseEntity.ok(customerService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer id){
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody Customer customer){
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @PutMapping
    public ResponseEntity<String> updateCustomer(@RequestBody CustomerDTO customer){
        return ResponseEntity.ok(customerService.updateCustomer(customer)? "Cap nhat thanh cong": "Cap nhat that bai");
    }

    @GetMapping("/daily-new-customers")
    public Integer getNewCustomers() {
        return customerService.getNewCustomers();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Integer id){
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Xoa khach hang co id: " + id + " thanh cong!");
    }
}
