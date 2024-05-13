package com.example.PromoCodes.controller;


import com.example.PromoCodes.dto.PurchaseResult;
import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.entity.Purchase;
import com.example.PromoCodes.exception.ProductNotFoundException;
import com.example.PromoCodes.service.ProductService;
import com.example.PromoCodes.service.PurchaseService;
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
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
public class PurchaseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private PurchaseService purchaseService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSimulatePurchase() throws Exception {
        long productId = 1L;
        String code = "TEST";
        Product product = new Product(productId, "Coffee Machine", "Makes great coffee", new BigDecimal("20.00"), Currency.EUR);
        Purchase purchase = new Purchase(1L,product, LocalDateTime.now(),new BigDecimal("10.00"),new BigDecimal("10.00"));
        PurchaseResult result = new PurchaseResult(purchase,"discount applied succesfully");

        when(productService.findProductById(productId)).thenReturn(Optional.of(product));
        when(purchaseService.simulatePurchase(product, code)).thenReturn(result);

        mockMvc.perform(post("/api/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":1,\"code\":\"TEST\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.purchase.discountApplied").value("10.0"))
                .andExpect(jsonPath("$.purchase.product.name").value("Coffee Machine"));
    }

    @Test
    public void testSimulatePurchase_ProductNotFound() throws Exception {
        long productId = 2L;

        when(productService.findProductById(productId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":2,\"code\":\"TEST\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ProductNotFoundException))
                .andExpect(result -> assertEquals("Product with id: " + productId + " does not exist", result.getResolvedException().getMessage()));
    }
}
