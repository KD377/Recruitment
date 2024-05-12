package com.example.PromoCodes.service;

import com.example.PromoCodes.dto.DiscountResult;
import com.example.PromoCodes.dto.SalesReport;
import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.dto.PurchaseResult;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.entity.Purchase;
import com.example.PromoCodes.repository.PromoCodeRepository;
import com.example.PromoCodes.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @Mock
    private DiscountService discountService;

    @Mock
    private PromoCodeService promoCodeService;

    @InjectMocks
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSimulatePurchase() {
        String code = "TEST";
        PromoCode promoCode = new PromoCode(1L, code, new Date(), new BigDecimal("10.0"), Currency.USD, 20, 0);
        Product product = new Product(1L, "Coffee Maker", "Makes great coffee", BigDecimal.valueOf(299.99), Currency.USD);

        DiscountResult discountResult = new DiscountResult(BigDecimal.valueOf(289.99), BigDecimal.valueOf(10.00), "Discount applied successfully", true);

        when(discountService.calculateDiscountedPrice(code, product)).thenReturn(discountResult);
        when(promoCodeRepository.findByCode(code)).thenReturn(Optional.of(promoCode));
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PurchaseResult result = purchaseService.simulatePurchase(product, code);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(289.99), result.getPurchase().getFinalPrice());
        assertEquals(BigDecimal.valueOf(10.00), result.getPurchase().getDiscountApplied());
        assertEquals("Discount applied successfully", result.getMessage());

        verify(discountService).calculateDiscountedPrice(code, product);
        verify(purchaseRepository).save(any(Purchase.class));
        verify(promoCodeService).increaseUsages(code);
    }

    @Test
    void testGenerateSalesReport() {
        SalesReport report1 = new SalesReport(Currency.USD, new BigDecimal("100"),new BigDecimal("20"), 10L);
        SalesReport report2 = new SalesReport(Currency.EUR, new BigDecimal("85"),new BigDecimal("20"), 8L);
        List<SalesReport> expectedReports = Arrays.asList(report1, report2);

        when(purchaseRepository.calculateSalesReport()).thenReturn(expectedReports);

        List<SalesReport> actualReports = purchaseService.generateSalesReport();

        assertNotNull(actualReports);
        assertEquals(expectedReports.size(), actualReports.size());
        assertEquals(expectedReports, actualReports);
        verify(purchaseRepository).calculateSalesReport();
    }
}
