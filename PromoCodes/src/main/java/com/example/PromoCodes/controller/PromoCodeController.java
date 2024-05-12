package com.example.PromoCodes.controller;

import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.service.PromoCodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/promo-code")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @Autowired
    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GetMapping
    public ResponseEntity<List<PromoCode>> getAllPromoCodes() {
        List<PromoCode> promoCodes = promoCodeService.getAllPromoCodes();
        return ResponseEntity.ok(promoCodes);
    }

    @PostMapping ResponseEntity<PromoCode> addPromoCode(@RequestBody @Valid PromoCode promoCode) {
        PromoCode newPromoCode = promoCodeService.createPromoCode(promoCode);
        return ResponseEntity.ok(newPromoCode);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getPromoCodeDetailsByCode(@PathVariable String code) {
        Optional<PromoCode> promoCode = promoCodeService.getPromoCodeByCode(code);
        return promoCode.map(promo -> ResponseEntity.ok((Object) promo))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("PromoCode: " + code + " does not exist"));
    }

}
