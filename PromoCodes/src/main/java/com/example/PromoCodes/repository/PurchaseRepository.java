package com.example.PromoCodes.repository;

import com.example.PromoCodes.dto.SalesReport;
import com.example.PromoCodes.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {
    @Query("SELECT new com.example.PromoCodes.dto.SalesReport(p.product.currency, SUM(p.finalPrice), SUM(p.discountApplied), COUNT(p)) FROM Purchase p GROUP BY p.product.currency")
    List<SalesReport> calculateSalesReport();
}
