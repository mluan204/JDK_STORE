package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT SUM(b.total_cost) FROM Bill b WHERE FUNCTION('DATE', b.created_at) = CURRENT_DATE")
    Integer getDailyRevenue();

    @Query("SELECT SUM(b.total_cost) FROM Bill b WHERE FUNCTION('DATE', b.created_at) = :date")
    Integer getYesterdayRevenue(@Param("date") java.time.LocalDate date);

    @Query("SELECT COUNT(b) FROM Bill b WHERE FUNCTION('DATE', b.created_at) = CURRENT_DATE")
    Integer getNumberOfBills();

    @Query("SELECT COUNT(b) FROM Bill b WHERE FUNCTION('DATE', b.created_at) = :date")
    Integer getYesterdayNumberOfBills(@Param("date") java.time.LocalDate date);

}
