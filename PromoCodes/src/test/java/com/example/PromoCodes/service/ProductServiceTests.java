package com.example.PromoCodes.service;

import com.example.PromoCodes.entity.Currency;
import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.exception.ProductNotFoundException;
import com.example.PromoCodes.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddProduct() {
        Product product = new Product(null, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.addProduct(product);
        assertNotNull(savedProduct);
        verify(productRepository).save(product);
    }
    @Test
    void testGetAllProducts() {
        Product product1 = new Product(null, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        Product product2 = new Product(null, "Another Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        List<Product> expectedProducts = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(expectedProducts);

        List<Product> actualProducts = productService.getAllProducts();

        assertEquals(expectedProducts, actualProducts);
        verify(productRepository).findAll();
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Optional<Product> product = Optional.of(new Product(null, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD));
        when(productRepository.findById(productId)).thenReturn(product);
        Optional<Product> foundProduct = productService.findProductById(productId);
        assertEquals(product.get(), foundProduct.get());
    }

    @Test
    void testUpdateProductSuccess() {
        Long productId = 1L;
        Product existingProduct = new Product(1L, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        Product newDetails = new Product(null, "Updated Espresso Machine", "Updated Description", new BigDecimal("1250.00"), Currency.EUR);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(newDetails);

        Product updatedProduct = productService.updateProduct(productId, newDetails);

        assertNotNull(updatedProduct);
        assertEquals("Updated Espresso Machine", updatedProduct.getName());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(new BigDecimal("1250.00"), updatedProduct.getRegularPrice());
        assertEquals(Currency.EUR, updatedProduct.getCurrency());
        verify(productRepository).save(existingProduct);
    }

    @Test
    void testUpdateProductNotFound() {
        Long productId = 2L;
        Product newDetails = new Product(null, "New Espresso Machine", "New Description", new BigDecimal("1300.00"), Currency.USD);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(productId, newDetails));

        assertEquals("Product with " + productId + " does not exist.", exception.getMessage());
    }
}
