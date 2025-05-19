package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {


    Optional<Object> findByName(String categoryName);
}
