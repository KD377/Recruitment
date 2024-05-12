package com.example.PromoCodes.dto;

import com.example.PromoCodes.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalesReport {
    private Currency currency;
    private BigDecimal totalAmount;
    private BigDecimal totalDiscount;
    private Long numberOfPurchases;
}
