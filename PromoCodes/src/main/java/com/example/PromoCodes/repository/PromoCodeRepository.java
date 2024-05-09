package com.example.PromoCodes.repository;

import com.example.PromoCodes.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface PromoCodeRepository extends JpaRepository<PromoCode,Long> {

    Optional<PromoCode> findByCode(String code);
}
