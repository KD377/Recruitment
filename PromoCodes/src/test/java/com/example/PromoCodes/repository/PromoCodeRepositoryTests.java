package com.example.PromoCodes.repository;

import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.PromoCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PromoCodeRepositoryTests {

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Test
    public void testSaveValidPromoCode() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("LATO123");
        promoCode.setExpirationDate(new Date());
        promoCode.setDiscountValue(BigDecimal.valueOf(45.00));
        promoCode.setCurrency(Currency.USD);
        promoCode.setMaxUsages(10);
        promoCode.setUsages(0);

        PromoCode savedPromoCode = promoCodeRepository.save(promoCode);

        assertNotNull(savedPromoCode.getId());
        assertEquals("LATO123", savedPromoCode.getCode());
        assertEquals(BigDecimal.valueOf(45.00), savedPromoCode.getDiscountValue());
    }


    @Test
    public void whenSavingCodeWithDuplicate_thenThrowException() {
        PromoCode firstPromoCode = new PromoCode();
        firstPromoCode.setCode("Duplicate");
        firstPromoCode.setExpirationDate(new Date());
        firstPromoCode.setDiscountValue(BigDecimal.valueOf(20.00));
        firstPromoCode.setCurrency(Currency.USD);
        firstPromoCode.setMaxUsages(3);
        firstPromoCode.setUsages(0);
        promoCodeRepository.saveAndFlush(firstPromoCode);

        PromoCode secondPromoCode = new PromoCode();
        secondPromoCode.setCode("Duplicate");
        secondPromoCode.setExpirationDate(new Date());
        secondPromoCode.setDiscountValue(BigDecimal.valueOf(25.00));
        secondPromoCode.setCurrency(Currency.USD);
        secondPromoCode.setMaxUsages(3);
        secondPromoCode.setUsages(0);

        assertThrows(DataIntegrityViolationException.class, () -> promoCodeRepository.saveAndFlush(secondPromoCode));
    }

    @Test
    public void findByCodeTest() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCode("LATO123");
        Date date =new Date();
        promoCode.setExpirationDate(date);
        promoCode.setDiscountValue(BigDecimal.valueOf(45.00));
        promoCode.setCurrency(Currency.USD);
        promoCode.setMaxUsages(10);

        promoCodeRepository.save(promoCode);

        Optional<PromoCode> foundPromoCode = promoCodeRepository.findByCode("LATO123");

        assertTrue(foundPromoCode.isPresent());
        PromoCode promo = foundPromoCode.get();
        assertEquals("LATO123", promo.getCode());
        assertEquals(date,promo.getExpirationDate());
        assertEquals(BigDecimal.valueOf(45.00).setScale(2), promo.getDiscountValue().setScale(2));
        assertEquals(Currency.USD, promo.getCurrency());
        assertEquals(10, promo.getMaxUsages());
        assertEquals(0, promo.getUsages());

    }


    @Test
    public void testIncrementUsages() {

        PromoCode promoCode = new PromoCode();
        assertEquals(0, promoCode.getUsages());

        promoCode.incrementUsages();

        assertEquals(1, promoCode.getUsages());

        promoCode.incrementUsages();

        assertEquals(2, promoCode.getUsages());
    }

}
