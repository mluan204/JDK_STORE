package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Integer> {
    @Query("SELECT SUM(bd.quantity) FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE FUNCTION('DATE', b.createdAt) = CURRENT_DATE AND b.isDeleted = false")
    Integer getTotalProductsSold();

    @Query("SELECT SUM(bd.quantity) FROM BillDetail bd " +
            "JOIN bd.bill b WHERE FUNCTION('DATE', b.createdAt) = :date AND b.isDeleted = false"
    )
    Integer getYesterdayProductsSold(@Param("date") java.time.LocalDate date
    );

    @Query("SELECT bd.bill.billDetails FROM BillDetail bd WHERE bd.bill.id = :billId AND bd.bill.isDeleted = false")
    List<BillDetail> findByBillId(@Param("billId") Integer billId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM BillDetail bd WHERE bd.bill.id = :billId")
    void deleteByBillId(@Param("billId") Integer billId);



}
