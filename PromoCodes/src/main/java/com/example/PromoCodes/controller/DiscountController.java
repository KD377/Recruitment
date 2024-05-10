package com.example.PromoCodes.controller;

import com.example.PromoCodes.dto.DiscountResult;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.service.DiscountService;
import com.example.PromoCodes.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/discount")
public class DiscountController {

    private final DiscountService discountService;

    private final ProductService productService;

    @Autowired
    public DiscountController(DiscountService discountService, ProductService productService) {
        this.discountService = discountService;
        this.productService = productService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<DiscountResult> getPriceAfterDiscount(@RequestParam String code, @PathVariable Long productId) {
        Optional<Product> product = productService.findProductById(productId);
        DiscountResult discountResult = discountService.calculatePriceAfterDiscount(code,product);
        return ResponseEntity.ok(discountResult);
    }
}
