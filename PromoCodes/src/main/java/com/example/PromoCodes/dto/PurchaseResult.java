package com.example.PromoCodes.dto;

import com.example.PromoCodes.entity.Purchase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseResult {
    private Purchase purchase;
    private String message;
}
