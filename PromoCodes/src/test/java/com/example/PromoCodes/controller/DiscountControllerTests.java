package com.example.PromoCodes.controller;

import com.example.PromoCodes.dto.DiscountResult;
import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.exception.ProductNotFoundException;
import com.example.PromoCodes.exception.PromoCodeNotFoundException;
import com.example.PromoCodes.service.DiscountService;
import com.example.PromoCodes.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DiscountController.class)
public class DiscountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiscountService discountService;

    @MockBean
    private ProductService productService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getPriceAfterDiscount_ProductExists_DiscountApplied() throws Exception {
        long productId = 1L;
        String code = "TEST";
        Product product = new Product(productId, "Coffee Maker", "Makes great coffee", BigDecimal.valueOf(20.00), Currency.USD);
        DiscountResult discountResult = new DiscountResult(BigDecimal.valueOf(10.00), BigDecimal.valueOf(10.00), "Discount applied successfully.", true);

        when(productService.findProductById(productId)).thenReturn(Optional.of(product));
        when(discountService.calculateDiscountedPrice(code, product)).thenReturn(discountResult);

        mockMvc.perform(post("/api/discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"code\":\"TEST\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.finalPrice").value("10.0"))
                .andExpect(jsonPath("$.discountAmount").value("10.0"))
                .andExpect(jsonPath("$.message").value("Discount applied successfully."));
    }

    @Test
    public void getPriceAfterDiscount_ProductNotFound_ThrowsException() throws Exception {
        long productId = 1L;

        when(productService.findProductById(productId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"code\":\"TEST\"}"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProductNotFoundException))
                .andExpect(result -> assertEquals("Product with id: 1 does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    public void getPriceAfterDiscount_PromoCodeNotFound_ThrowsException() throws Exception {
        Product product = new Product(1L,"Coffee machine",null,new BigDecimal(20),Currency.EUR);
        String code ="TEST";

        when(productService.findProductById(1L)).thenReturn(Optional.of(product));
        when(discountService.calculateDiscountedPrice(code,product)).thenThrow(new PromoCodeNotFoundException("Promo code not found"));

        mockMvc.perform(post("/api/discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"code\":\"TEST\"}"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PromoCodeNotFoundException))
                .andExpect(result -> assertEquals("Promo code not found", result.getResolvedException().getMessage()));
    }
}
