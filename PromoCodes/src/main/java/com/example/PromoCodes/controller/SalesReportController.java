package com.example.PromoCodes.controller;

import com.example.PromoCodes.dto.SalesReport;
import com.example.PromoCodes.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sales-report")
public class SalesReportController {

    private final PurchaseService purchaseService;

    @Autowired
    public SalesReportController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public ResponseEntity<List<SalesReport>> getSalesReport() {
        List<SalesReport> report = purchaseService.generateSalesReport();
        return ResponseEntity.ok(report);
    }
}
