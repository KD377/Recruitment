package com.example.PromoCodes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{3,24}$", message = "Code must be alphanumeric and 3-24 characters long without whitespaces.")
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
    @Min(value = 1, message = "amount of max usages must be positive")
    private int maxUsages;

    @Column(nullable = false)
    @JsonIgnore
    private int usages = 0;
}
