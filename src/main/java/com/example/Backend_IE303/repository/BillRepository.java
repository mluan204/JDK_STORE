package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

        @Query(value = "SELECT COALESCE(SUM(b.after_discount), 0) FROM Bill b WHERE DATE(b.created_at) = CURRENT_DATE AND b.is_deleted = false", nativeQuery = true)
        Integer getDailyRevenue();

        @Query(value = "SELECT COALESCE(SUM(b.after_discount), 0) FROM Bill b WHERE DATE(b.created_at) = :date AND b.is_deleted = false", nativeQuery = true)
        Integer getYesterdayRevenue(@Param("date") java.time.LocalDate date);

        @Query(value = "SELECT COUNT(*) FROM Bill b WHERE DATE(b.created_at) = CURRENT_DATE AND b.is_deleted = false", nativeQuery = true)
        Integer getNumberOfBills();

        @Query(value = "SELECT COUNT(*) FROM Bill b WHERE DATE(b.created_at) = :date AND b.is_deleted = false", nativeQuery = true)
        Integer getYesterdayNumberOfBills(@Param("date") java.time.LocalDate date);

        @Query(value = "SELECT b FROM Bill b " +
                        "WHERE (" +
                        "LOWER(b.employee.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(b.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "CAST(b.id AS string) LIKE CONCAT('%', :keyword, '%')" +
                        ") AND b.isDeleted = false")
        Page<Bill> searchBillsByKeyword(
                        @Param("keyword") String keyword,
                        Pageable pageable);

        Page<Bill> findByCreatedAtBetweenAndIsDeletedFalse(LocalDateTime start, LocalDateTime end, Pageable pageable);

        @Query(value = "SELECT b FROM Bill b " +
                        "WHERE (" +
                        "LOWER(b.employee.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(b.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "CAST(b.id AS string) LIKE CONCAT('%', :keyword, '%')" +
                        ") AND b.createdAt BETWEEN :start AND :end AND b.isDeleted = false")
        Page<Bill> searchBillsByKeywordAndCreatedAtBetween(
                        @Param("keyword") String keyword,
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end,
                        Pageable pageable);

        @Query(value = "SELECT b FROM Bill b WHERE b.createdAt BETWEEN :startDate AND :endDate AND b.isDeleted = false")
        List<Bill> findByDateRange(
                        @Param("startDate") Timestamp startDate,
                        @Param("endDate") Timestamp endDate);

        @Query(value = "SELECT COALESCE(SUM(b.after_discount), 0) FROM Bill b WHERE b.createdAt BETWEEN :startDate AND :endDate AND b.isDeleted = false", nativeQuery = true)
        Integer getTotalRevenue(
                        @Param("startDate") Timestamp startDate,
                        @Param("endDate") Timestamp endDate);
}
