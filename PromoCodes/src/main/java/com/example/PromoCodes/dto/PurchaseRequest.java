package com.example.PromoCodes.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PurchaseRequest {
    @NotNull(message = "productId must be provided")
    private Long productId;
    @NotNull (message = "Promo Code must be provided")
    private String code;
}
