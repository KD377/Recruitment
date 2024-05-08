package com.example.PromoCodes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min=3,max=24)
    private String code;

    @Column(nullable = false)
    private Date expirationDate;

    @Column(nullable = false,precision = 10,scale = 2)
    @Min(value = 1, message = "discount must be greater than 0")
    private BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    @Min(value = 0, message = "amount of max usages must be positive")
    private int maxUsages;

    @Column(nullable = false)
    private int usages = 0;
}
