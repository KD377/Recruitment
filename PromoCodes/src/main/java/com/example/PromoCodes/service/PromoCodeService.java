package com.example.PromoCodes.service;

import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.repository.PromoCodeRepository;
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
        return this.promoCodeRepository.save(promoCode);
    }
}
