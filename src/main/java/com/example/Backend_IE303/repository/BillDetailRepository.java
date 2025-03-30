package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {
    @Query("SELECT SUM(bd.quantity) FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE FUNCTION('DATE', b.created_at) = CURRENT_DATE AND b.isDeleted = false")
    Integer getTotalProductsSold();

    @Query("SELECT SUM(bd.quantity) FROM BillDetail bd " +
            "JOIN bd.bill b WHERE FUNCTION('DATE', b.created_at) = :date AND b.isDeleted = false"
    )
    Integer getYesterdayProductsSold(@Param("date") java.time.LocalDate date
    );

    @Query("SELECT bd.bill.billDetails FROM BillDetail bd WHERE bd.bill.id = :billId AND bd.bill.isDeleted = false")
    List<BillDetail> findByBillId(@Param("billId") Integer billId);


}
