package com.example.Backend_IE303.service;

import com.example.Backend_IE303.dto.CategoryDTO;
import com.example.Backend_IE303.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
