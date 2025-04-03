package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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

}
