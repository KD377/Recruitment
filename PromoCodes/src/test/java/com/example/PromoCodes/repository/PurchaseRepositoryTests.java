package com.example.PromoCodes.repository;

import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.entity.Purchase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class PurchaseRepositoryTests {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired ProductRepository productRepository;

    @Test
    public void testSavePurchase() {
        Purchase purchase = new Purchase();
        Product product = new Product(null, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        productRepository.save(product);
        purchase.setProduct(product);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setFinalPrice(new BigDecimal("100.00"));
        purchase.setDiscountApplied(new BigDecimal("10.00"));

        Purchase savedPurchase = purchaseRepository.save(purchase);
        assertNotNull(savedPurchase);
        assertNotNull(savedPurchase.getId());

    }

    @Test
    public void parameterConstructorTest() {
        Purchase purchase = new Purchase();
        Product product = new Product(null, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        productRepository.save(product);
        purchase.setProduct(product);
        LocalDateTime now = LocalDateTime.now();
        purchase.setPurchaseDate(now);
        purchase.setFinalPrice(new BigDecimal("100.00"));
        purchase.setDiscountApplied(new BigDecimal("10.00"));


        assertEquals(product,purchase.getProduct());
        assertEquals(now,purchase.getPurchaseDate());
        assertEquals(new BigDecimal("100.00"),purchase.getFinalPrice());
        assertEquals(new BigDecimal("10.00"),purchase.getDiscountApplied());
    }

    @Test
    public void findByIdTest() {
        Purchase purchase = new Purchase();
        Product product = new Product(null, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        productRepository.save(product);
        purchase.setProduct(product);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setFinalPrice(new BigDecimal("100.00"));
        purchase.setDiscountApplied(new BigDecimal("10.00"));
        Purchase savedPurchase = purchaseRepository.save(purchase);

        Optional<Purchase> foundPurchase = purchaseRepository.findById(savedPurchase.getId());
        assertTrue(foundPurchase.isPresent());
        assertEquals(savedPurchase.getId(),foundPurchase.get().getId());
    }

}
