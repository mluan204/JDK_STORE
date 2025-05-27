package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Combo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Integer> {
    @Query("SELECT c FROM Combo c WHERE (:isActive IS NULL OR " +
            "(:isActive = true AND c.timeEnd > :currentTime) OR " +
            "(:isActive = false AND c.timeEnd <= :currentTime))")
    Page<Combo> findAllByExpirationStatus(
            @Param("isActive") Boolean isActive,
            @Param("currentTime") Timestamp currentTime,
            Pageable pageable);
}
