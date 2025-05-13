package com.example.Backend_IE303.dto;

import com.example.Backend_IE303.entity.Category;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Data
public class CategoryDTO {

    private Integer id;
    private String name;


    public CategoryDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public  static CategoryDTO fromEntity(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }
}
