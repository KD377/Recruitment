package com.example.PromoCodes.service;

import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.exception.PromoCodeNotFoundException;
import com.example.PromoCodes.repository.PromoCodeRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PromoCodeServiceTests {

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @InjectMocks
    private PromoCodeService promoCodeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreatePromoCode() {
        PromoCode promoCode = new PromoCode(null,"TEST",new Date(),new BigDecimal("10.00"), Currency.USD,10,0);
        when(promoCodeRepository.save(any(PromoCode.class))).thenReturn(promoCode);

        PromoCode savedPromoCode = promoCodeService.createPromoCode(promoCode);
        assertNotNull(savedPromoCode);
        verify(promoCodeRepository).save(promoCode);
    }

    @Test
    void testGetAllProducts() {
        PromoCode promoCode1 = new PromoCode(null,"TEST",new Date(),new BigDecimal("10.00"), Currency.USD,10,0);
        PromoCode promoCode2 = new PromoCode(null,"TEST1",new Date(),new BigDecimal("10.00"), Currency.USD,10,0);
        List<PromoCode> expectedPromoCodes = Arrays.asList(promoCode1, promoCode2);

        when(promoCodeRepository.findAll()).thenReturn(expectedPromoCodes);

        List<PromoCode> actualPromoCodes = promoCodeService.getAllPromoCodes();

        assertEquals(expectedPromoCodes, actualPromoCodes);
        verify(promoCodeRepository).findAll();
    }

    @Test
    void testGetPromoCodeByCode() {
        String code = "TEST";
        PromoCode promoCode = new PromoCode(1L, code, new Date(), new BigDecimal("10.00"), Currency.USD, 100, 0);
        when(promoCodeRepository.findByCode(code)).thenReturn(Optional.of(promoCode));

        Optional<PromoCode> result = promoCodeService.getPromoCodeByCode(code);

        assertTrue(result.isPresent());
        assertEquals(code, result.get().getCode());
        verify(promoCodeRepository).findByCode(code);

    }

    @Test
    void testIncreaseUsagesSuccess() {
        String code = "TEST";
        int usages = 0;
        PromoCode promoCode = new PromoCode(1L, code, new Date(), new BigDecimal("10.00"), Currency.USD, 100, usages);
        when(promoCodeRepository.findByCode(code)).thenReturn(Optional.of(promoCode));

        promoCodeService.increaseUsages(code);

        assertEquals(usages + 1, promoCode.getUsages());
        verify(promoCodeRepository).save(promoCode);
    }

    @Test
    void testIncreaseUsagesNotFound() {
        String code = "TEST";
        when(promoCodeRepository.findByCode(code)).thenReturn(Optional.empty());

        PromoCodeNotFoundException thrown = assertThrows(PromoCodeNotFoundException.class, () -> {
            promoCodeService.increaseUsages(code);
        });

        assertEquals("Promo code not found with code: " + code, thrown.getMessage());
        verify(promoCodeRepository, never()).save(any(PromoCode.class));
    }

}
