package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {

    Page<Receipt> findAll(Specification<Receipt> spec, Pageable pageable);
}
