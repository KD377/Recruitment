package com.example.PromoCodes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_code_id")
    private PromoCode promoCode;

    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @Column(nullable = false, scale = 2, precision = 10)
    private BigDecimal regularPrice;

    @Column(scale = 2, precision = 10)
    @Min(value = 0)
    private BigDecimal discountApplied;

    @Column(nullable = false, scale = 2, precision = 10)
    private BigDecimal finalPrice;

}
