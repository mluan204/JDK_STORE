package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
