package com.example.PromoCodes.repository;

import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.entity.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        Product product = new Product();
        product.setId(null);
        product.setName("Espresso Machine");
        product.setDescription("Makes strong coffee");
        product.setRegularPrice(new BigDecimal("1200.00"));
        product.setCurrency(Currency.USD);
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

}
