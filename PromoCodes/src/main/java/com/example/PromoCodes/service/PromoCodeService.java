package com.example.PromoCodes.service;

import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.exception.PromoCodeNotFoundException;
import com.example.PromoCodes.repository.PromoCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    @Autowired
    public PromoCodeService(PromoCodeRepository promoCodeRepository){
        this.promoCodeRepository = promoCodeRepository;
    }

    public List<PromoCode> getAllPromoCodes() {
        return this.promoCodeRepository.findAll();
    }

    public Optional<PromoCode> getPromoCodeByCode(String code) {
        return this.promoCodeRepository.findByCode(code);
    }

    public PromoCode createPromoCode(PromoCode promoCode) {

        Date expirationDate = promoCode.getExpirationDate();
        if (expirationDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(expirationDate);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            promoCode.setExpirationDate(calendar.getTime());
        }

        return promoCodeRepository.save(promoCode);
    }

    @Transactional
    public void increaseUsages(String code) {
        Optional<PromoCode> promoCodeOptional = promoCodeRepository.findByCode(code);
        if (promoCodeOptional.isEmpty()) {
            throw new PromoCodeNotFoundException("Promo code not found with code: " + code);
        }
        PromoCode promoCode = promoCodeOptional.get();
        promoCode.incrementUsages();
        promoCodeRepository.save(promoCode);

    }
}
