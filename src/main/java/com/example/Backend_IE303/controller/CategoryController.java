package com.example.Backend_IE303.controller;

import com.example.Backend_IE303.dto.CategoryDTO;
import com.example.Backend_IE303.entity.Category;
import com.example.Backend_IE303.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //Lấy danh sách các loại sản phẩm
    @GetMapping("/all")  // Thêm annotation để ánh xạ GET request
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
