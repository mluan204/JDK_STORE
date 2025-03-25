package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
    @Query("SELECT SUM(bd.quantity) FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE FUNCTION('DATE', b.created_at) = CURRENT_DATE")
    Integer getTotalProductsSold();

    @Query("SELECT SUM(bd.quantity) FROM BillDetail bd " +
            "JOIN bd.bill b WHERE FUNCTION('DATE', b.created_at) = :date"
    )
    Integer getYesterdayProductsSold(@Param("date") java.time.LocalDate date
    );

}
