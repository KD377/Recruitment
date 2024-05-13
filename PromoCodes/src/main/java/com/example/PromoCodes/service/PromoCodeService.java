package com.example.PromoCodes.service;

import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.exception.PromoCodeNotFoundException;
import com.example.PromoCodes.repository.PromoCodeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
