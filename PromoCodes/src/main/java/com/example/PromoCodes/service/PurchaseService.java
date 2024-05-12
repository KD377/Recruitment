package com.example.PromoCodes.service;

import com.example.PromoCodes.dto.DiscountResult;
import com.example.PromoCodes.dto.PurchaseResult;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.entity.Purchase;
import com.example.PromoCodes.repository.PurchaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PurchaseService {

    private final DiscountService discountService;
    private final PromoCodeService promoCodeService;

    private final PurchaseRepository purchaseRepository;

    @Autowired
    public PurchaseService(DiscountService discountService, ProductService productService, PurchaseRepository purchaseRepository,PromoCodeService promoCodeService) {
        this.discountService = discountService;
        this.promoCodeService = promoCodeService;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public PurchaseResult simulatePurchase(Product product, String code) {

        DiscountResult discountResult = discountService.calculateDiscountedPrice(code,product);
        Purchase purchase = new Purchase();
        purchase.setProduct(product);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setFinalPrice(discountResult.getFinalPrice());
        purchase.setDiscountApplied(discountResult.getDiscountAmount());

        purchaseRepository.save(purchase);
        promoCodeService.increaseUsages(code);

        return new PurchaseResult(purchase,discountResult.getMessage());

    }
}
