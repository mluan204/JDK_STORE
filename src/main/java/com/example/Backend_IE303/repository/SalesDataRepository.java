package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.SalesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesDataRepository extends JpaRepository<SalesData, Long> {

    @Query("SELECT s FROM SalesData s WHERE s.createdAt BETWEEN :startDate AND :endDate AND s.type = :type")
    List<SalesData> findByDateRangeAndType(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("type") String type);
}