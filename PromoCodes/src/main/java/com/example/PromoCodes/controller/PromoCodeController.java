package com.example.PromoCodes.controller;

import com.example.PromoCodes.entity.PromoCode;
import com.example.PromoCodes.service.PromoCodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @PostMapping ResponseEntity<?> addPromoCode(@RequestBody @Valid PromoCode promoCode) {
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




    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        Map<String, Object> response = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });
        response.put("status_code", HttpStatus.BAD_REQUEST.value());
        response.put("status",HttpStatus.BAD_REQUEST);
        response.put("errors", errors);
        return response;
    }

}
