package com.example.Backend_IE303.service;

import com.example.Backend_IE303.entity.Employee;
import com.example.Backend_IE303.entity.Receipt;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ReceiptSpecification {

    public static Specification<Receipt> hasCreatedDateBetween(LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) -> {
            if (fromDate == null && toDate == null) return null;

            if (fromDate != null && toDate != null) {
                return cb.between(cb.function("DATE", LocalDate.class, root.get("created_at")), fromDate, toDate);
            } else if (fromDate != null) {
                return cb.greaterThanOrEqualTo(cb.function("DATE", LocalDate.class, root.get("created_at")), fromDate);
            } else {
                return cb.lessThanOrEqualTo(cb.function("DATE", LocalDate.class, root.get("created_at")), toDate);
            }
        };
    }

    public static Specification<Receipt> hasEmployeeName(String employeeName) {
        return (root, query, cb) -> {
            if (employeeName == null || employeeName.trim().isEmpty()) return null;

            // Join với bảng employee
            Join<Object, Object> employeeJoin = root.join("employee", JoinType.LEFT);

            // Tìm tên gần đúng (không phân biệt hoa thường)
            return cb.like(cb.lower(employeeJoin.get("name")), "%" + employeeName.toLowerCase() + "%");
        };
    }

}
