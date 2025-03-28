package com.example.Backend_IE303.repository;

import com.example.Backend_IE303.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("SELECT COUNT(c.id) FROM Customer c " +
            "WHERE FUNCTION('DATE', c.created_at) = CURRENT_DATE")
    Integer getNewCustomers();

    @Query("SELECT COUNT(c.id) FROM Customer c WHERE FUNCTION('DATE', c.created_at) = :date")
    Integer getYesterdayNewCustomers(@Param("date") java.time.LocalDate date);

}
