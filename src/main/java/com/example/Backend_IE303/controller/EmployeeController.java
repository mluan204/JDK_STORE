package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.EmployeeDTO;
import com.example.Backend_IE303.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService service;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Integer id){
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO){
        return ResponseEntity.ok(service.updateEmployee(id,employeeDTO));
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO dto){
        return ResponseEntity.ok(service.createEmployee(dto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Integer id){
        service.deleteEmployee(id);
        return ResponseEntity.ok("Xoa khach hang co id: " + id + " thanh cong!");
    }

}
