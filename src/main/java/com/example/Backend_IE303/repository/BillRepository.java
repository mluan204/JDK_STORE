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
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query("SELECT SUM(b.after_discount) FROM Bill b WHERE FUNCTION('DATE', b.createdAt) = CURRENT_DATE AND b.isDeleted = false")
    Integer getDailyRevenue();

    @Query("SELECT SUM(b.after_discount) FROM Bill b WHERE FUNCTION('DATE', b.createdAt) = :date AND b.isDeleted = false")
    Integer getYesterdayRevenue(@Param("date") java.time.LocalDate date);

    @Query("SELECT COUNT(b) FROM Bill b WHERE FUNCTION('DATE', b.createdAt) = CURRENT_DATE AND b.isDeleted = false")
    Integer getNumberOfBills();

    @Query("SELECT COUNT(b) FROM Bill b WHERE FUNCTION('DATE', b.createdAt) = :date AND b.isDeleted = false")
    Integer getYesterdayNumberOfBills(@Param("date") java.time.LocalDate date);

//    List<Bill> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
//
    @Query("SELECT b FROM Bill b " +
            "WHERE (" +
            "LOWER(b.employee.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "STR(b.id) LIKE CONCAT('%', :keyword, '%')" +
            ") "
          )
    Page<Bill> searchBillsByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Page<Bill> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT b FROM Bill b " +
            "WHERE (" +
            "LOWER(b.employee.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.customer.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "STR(b.id) LIKE CONCAT('%', :keyword, '%')" +
            ") AND b.createdAt BETWEEN :start AND :end")
    Page<Bill> searchBillsByKeywordAndCreatedAtBetween(
            @Param("keyword") String keyword,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

}
