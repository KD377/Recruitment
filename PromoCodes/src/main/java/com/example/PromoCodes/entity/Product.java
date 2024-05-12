package com.example.PromoCodes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @Min(value = 1, message = "Price must be greater than 0")
    @NotNull (message = "regular price must be provided")
    private BigDecimal regularPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Currency must be provided")
    private Currency currency;


}
