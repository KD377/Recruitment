package com.example.PromoCodes.controller;

import com.example.PromoCodes.dto.PurchaseRequest;
import com.example.PromoCodes.dto.PurchaseResult;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.exception.ProductNotFoundException;
import com.example.PromoCodes.service.ProductService;
import com.example.PromoCodes.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final ProductService productService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, ProductService productService) {
        this.purchaseService = purchaseService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<PurchaseResult> simulatePurchase(@RequestBody PurchaseRequest purchaseRequest) {
        Optional<Product> productOptional = productService.findProductById(purchaseRequest.getProductId());
        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Product with id: " + purchaseRequest.getProductId() + " does not exist" );
        }
        Product product = productOptional.get();
        PurchaseResult purchaseResult = purchaseService.simulatePurchase(product,purchaseRequest.getCode());

        return ResponseEntity.ok(purchaseResult);
    }
}
