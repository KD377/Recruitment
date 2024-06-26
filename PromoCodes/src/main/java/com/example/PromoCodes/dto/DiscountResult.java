package com.example.PromoCodes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DiscountResult {

    private BigDecimal finalPrice;
    private BigDecimal discountAmount;
    private String message;
    private boolean isDiscountSuccesful;

}
