package com.example.PromoCodes.repository;

import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        Product product = new Product(null, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        Product savedProduct = productRepository.save(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Espresso Machine");
    }

    @Test
    public void testFindProductById() {
        Product product = new Product(null, "Espresso Machine", "Makes strong coffee", new BigDecimal("1200.00"), Currency.USD);
        product = productRepository.save(product);
        Optional<Product> foundProduct = productRepository.findById(product.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Espresso Machine");
    }

    @Test
    public void testUpdateProduct() {
        Product product = new Product(null, "Drip Coffee Maker", "Standard coffee maker", new BigDecimal("80.00"), Currency.USD);
        product = productRepository.save(product);
        product.setName("Updated Drip Coffee Maker");
        Product updatedProduct = productRepository.save(product);
        assertThat(updatedProduct.getName()).isEqualTo("Updated Drip Coffee Maker");
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product(null, "French Press", "For brewing coffee or tea", new BigDecimal("25.00"), Currency.USD);
        product = productRepository.save(product);
        assertThat(productRepository.findById(product.getId())).isPresent();
        productRepository.delete(product);
        assertThat(productRepository.findById(product.getId())).isNotPresent();
    }

}
