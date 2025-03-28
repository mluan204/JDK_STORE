package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
