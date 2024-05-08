package com.example.PromoCodes.repository;

import com.example.PromoCodes.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoCodeRepository extends JpaRepository<PromoCode,Long> {
}
