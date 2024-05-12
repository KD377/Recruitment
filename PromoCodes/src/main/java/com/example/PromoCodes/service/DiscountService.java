package com.example.PromoCodes.service;

import com.example.PromoCodes.dto.DiscountResult;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.exception.PromoCodeNotFoundException;
import com.example.PromoCodes.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Service
public class DiscountService {

    private final PromoCodeRepository promoCodeRepository;

    @Autowired
    public DiscountService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public DiscountResult calculateDiscountedPrice(String code, Product product) {

        Optional<PromoCode> foundPromoCode = promoCodeRepository.findByCode(code);

        if (foundPromoCode.isEmpty()) {
            throw new PromoCodeNotFoundException("Promo code not found");
        }

        PromoCode promoCode = foundPromoCode.get();

        if (promoCode.getExpirationDate().before(new Date())) {
            return new DiscountResult(product.getRegularPrice(),new BigDecimal(0),"Promo code expired",false);
        }

        if (!promoCode.getCurrency().equals(product.getCurrency())) {
            return new DiscountResult(product.getRegularPrice(),new BigDecimal(0),"Promo code is not applicable for this product. Currencies does not match. Product currency: " + product.getCurrency()+ " PromoCode currency: "+ promoCode.getCurrency(),false);
        }

        if (promoCode.getUsages() >= promoCode.getMaxUsages()) {
            return new DiscountResult(product.getRegularPrice(),new BigDecimal(0),"Max usages reached. Promo code is no longer valid",false);
        }


        BigDecimal discountedPrice = product.getRegularPrice();
        if (promoCode.getDiscountValue() != null) {
            discountedPrice = product.getRegularPrice().subtract(promoCode.getDiscountValue());
        }

        discountedPrice = discountedPrice.max(BigDecimal.ZERO);

        return new DiscountResult(discountedPrice,promoCode.getDiscountValue(), "Discount applied successfully.",true);


    }
}
