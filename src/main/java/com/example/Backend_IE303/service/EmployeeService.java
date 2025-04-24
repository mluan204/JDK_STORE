package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.EmployeeDTO;
import com.example.Backend_IE303.entity.Employee;
import com.example.Backend_IE303.exceptions.ApiError;
import com.example.Backend_IE303.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeDTO> getAll(){
        return repository.findAll().stream().map(EmployeeDTO::new).toList();
    }

    public EmployeeDTO getById(Integer id){
        return new EmployeeDTO(repository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay id")));
    }

    public EmployeeDTO createEmployee(EmployeeDTO dto){
        dto.setCreated_at(new Timestamp(System.currentTimeMillis()));
        return new EmployeeDTO(repository.save(dto.mappingToEmployee()));
    }

    public EmployeeDTO updateEmployee(Integer id,EmployeeDTO employeeDTO){
        Employee e = repository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay Employee voi id: " + employeeDTO.getId()));
        e.setName(employeeDTO.getName());
        e.setPosition(employeeDTO.getPosition());
        e.setEmail(employeeDTO.getEmail());
        e.setGender(employeeDTO.isGender());
        e.setAddress(employeeDTO.getAddress());
        e.setSalary(employeeDTO.getSalary());
        e.setImage(employeeDTO.getImage());
        e.setPhone_number(employeeDTO.getPhone_number());
        e.setBirthday(employeeDTO.getBirthday());
        repository.save(e);
        return new EmployeeDTO(e);
    }

    public void deleteEmployee(Integer id){
        repository.deleteById(id);
    }

}
