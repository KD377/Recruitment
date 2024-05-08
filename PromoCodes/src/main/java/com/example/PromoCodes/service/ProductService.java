package com.example.PromoCodes.service;

import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @Transactional
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Optional<Product> updateProduct(Long id, Product changedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(changedProduct.getName());
            product.setDescription(changedProduct.getDescription());
            product.setPrice(changedProduct.getPrice());
            product.setCurrency(changedProduct.getCurrency());
            return productRepository.save(product);
        });
    }
}
