package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Object> findByName(String employeeName);
}
