package com.example.PromoCodes.service;

import com.example.PromoCodes.dto.DiscountResult;
import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.exception.PromoCodeNotFoundException;
import com.example.PromoCodes.repository.PromoCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DiscountServiceTests {

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @InjectMocks
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCalculateDiscountedPrice() {
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.DAY_OF_MONTH, 1);
        PromoCode promoCode = new PromoCode(1L,"TEST",futureDate.getTime(),new BigDecimal(20), Currency.EUR,20,0);
        Product product = new Product(1L,"Coffe machine","Makes strong coffe",new BigDecimal(100),Currency.EUR);
        when(promoCodeRepository.findByCode("TEST")).thenReturn(Optional.of(promoCode));

        DiscountResult discountResult = discountService.calculateDiscountedPrice("TEST",product);

        assertNotNull(discountResult);
        assertEquals("Discount applied successfully.",discountResult.getMessage());
        assertEquals(product.getRegularPrice().subtract(promoCode.getDiscountValue()),discountResult.getFinalPrice());
        assertEquals(promoCode.getDiscountValue(),discountResult.getDiscountAmount());
        assertTrue(discountResult.isDiscountSuccesful());
    }

    @Test
    void testCalculateDiscountedPrice_PromoCodeNotFound() {
        String code = "TEST";
        Product product = new Product(1L, "Coffee Maker", "Makes great coffee", BigDecimal.valueOf(20), Currency.USD);

        when(promoCodeRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThrows(PromoCodeNotFoundException.class, () -> discountService.calculateDiscountedPrice(code, product));
    }

    @Test
    void testCalculateDiscountedPrice_PromoCodeExpired() {
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.DAY_OF_MONTH, -1);
        PromoCode promoCode = new PromoCode(1L,"TEST",futureDate.getTime(),new BigDecimal(20), Currency.EUR,20,0);
        Product product = new Product(1L,"Coffe machine","Makes strong coffe",new BigDecimal(100),Currency.EUR);
        when(promoCodeRepository.findByCode("TEST")).thenReturn(Optional.of(promoCode));

        DiscountResult discountResult = discountService.calculateDiscountedPrice("TEST",product);

        assertNotNull(discountResult);
        assertEquals("Promo code expired",discountResult.getMessage());
        assertEquals(BigDecimal.ZERO,discountResult.getDiscountAmount());
        assertEquals(product.getRegularPrice(),discountResult.getFinalPrice());
        assertFalse(discountResult.isDiscountSuccesful());
    }

    @Test
    void testCalculateDiscountedPrice_CurrenciesDoesNotMatch() {
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.DAY_OF_MONTH, 1);
        PromoCode promoCode = new PromoCode(1L,"TEST",futureDate.getTime(),new BigDecimal(20), Currency.EUR,20,0);
        Product product = new Product(1L,"Coffe machine","Makes strong coffe",new BigDecimal(100),Currency.USD);
        when(promoCodeRepository.findByCode("TEST")).thenReturn(Optional.of(promoCode));

        DiscountResult discountResult = discountService.calculateDiscountedPrice("TEST",product);

        assertNotNull(discountResult);
        assertEquals("Promo code is not applicable for this product. Currencies does not match. Product currency: " + product.getCurrency()+ " PromoCode currency: "+ promoCode.getCurrency(),
                discountResult.getMessage());
        assertEquals(BigDecimal.ZERO,discountResult.getDiscountAmount());
        assertEquals(product.getRegularPrice(),discountResult.getFinalPrice());
        assertFalse(discountResult.isDiscountSuccesful());

    }

    @Test
    void testCalculateDiscountedPrice_MaxUsagesReached() {
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.DAY_OF_MONTH, 1);
        PromoCode promoCode = new PromoCode(1L,"TEST",futureDate.getTime(),new BigDecimal(20), Currency.EUR,0,0);
        Product product = new Product(1L,"Coffe machine","Makes strong coffe",new BigDecimal(100),Currency.EUR);
        when(promoCodeRepository.findByCode("TEST")).thenReturn(Optional.of(promoCode));

        DiscountResult discountResult = discountService.calculateDiscountedPrice("TEST",product);

        assertNotNull(discountResult);
        assertEquals("Max usages reached. Promo code is no longer valid",discountResult.getMessage());
        assertEquals(BigDecimal.ZERO,discountResult.getDiscountAmount());
        assertEquals(product.getRegularPrice(),discountResult.getFinalPrice());
        assertFalse(discountResult.isDiscountSuccesful());
    }


}
