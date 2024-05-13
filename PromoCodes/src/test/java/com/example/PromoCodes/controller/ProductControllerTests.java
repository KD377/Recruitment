package com.example.PromoCodes.controller;

import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.Product;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(new Product(1L, "Coffee Maker", "Makes great coffee", BigDecimal.valueOf(20.00), Currency.USD));
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Coffee Maker"));
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product createdProduct = new Product(1L, "Coffee Maker", "Makes great coffee", BigDecimal.valueOf(20.00), Currency.USD);

        when(productService.addProduct(any(Product.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Coffee Maker\",\"description\":\"Makes great coffee\",\"regularPrice\":20.00,\"currency\":\"USD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Coffee Maker"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Long productId = 1L;
        Product changedProduct = new Product(productId, "Updated Coffee Maker", "Updated Description", BigDecimal.valueOf(25.00), Currency.USD);
        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(changedProduct);

        mockMvc.perform(put("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Coffee Maker\",\"description\":\"Updated Description\",\"regularPrice\":25.00,\"currency\":\"USD\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Coffee Maker"));
    }

    @Test
    public void testCreateProduct_IncorrectType() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Coffee Maker\",\"description\":\"Makes great coffee\",\"regularPrice\":\"Makes great coffee\",\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.regularPrice").value("Invalid value for 'regularPrice': 'Makes great coffee'. Expected type: BigDecimal"));
    }

    @Test
    public void testCreateProduct_NotAllValuesProvided() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Coffee Maker\",\"description\":\"Makes great coffee\",\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.regularPrice").value("regular price must be provided"));
    }

    @Test
    public void testCreateProduct_PriceBelowZero() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Coffee Maker\",\"description\":\"Updated Description\",\"regularPrice\":-15.00,\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.regularPrice").value("Price must be greater than 0"));
    }
}
