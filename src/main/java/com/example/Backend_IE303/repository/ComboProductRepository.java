package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.ComboProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ComboProductRepository extends JpaRepository<ComboProduct, Integer> {
    @Query("SELECT cp FROM ComboProduct cp WHERE cp.combo.id = :comboId")
    List<ComboProduct> findByComboId(@Param("comboId") Integer comboId);
}
