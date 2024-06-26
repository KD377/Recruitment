package com.example.PromoCodes.controller;

import com.example.PromoCodes.dto.DiscountResult;
import com.example.PromoCodes.dto.PurchaseRequest;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.exception.ProductNotFoundException;
import com.example.PromoCodes.service.DiscountService;
import com.example.PromoCodes.service.ProductService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<DiscountResult> getPriceAfterDiscount(@Valid @RequestBody PurchaseRequest purchaseRequest) {
        Optional<Product> productOptional = productService.findProductById(purchaseRequest.getProductId());
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Product with id: " + purchaseRequest.getProductId() + " does not exist" );
        }
        Product product = productOptional.get();
        DiscountResult discountResult = discountService.calculateDiscountedPrice(purchaseRequest.getCode(), product);
        return ResponseEntity.ok(discountResult);
    }
}
