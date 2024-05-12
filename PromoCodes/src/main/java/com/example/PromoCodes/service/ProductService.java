package com.example.PromoCodes.service;

import com.example.PromoCodes.entity.Product;
import com.example.PromoCodes.exception.ProductNotFoundException;
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

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product changedProduct) {
        Optional<Product> foundProduct = productRepository.findById(id);
        if(foundProduct.isEmpty()) {
            throw new ProductNotFoundException("Product with " + id + " does not exist.");
        }
        Product product = foundProduct.get();
        product.setName(changedProduct.getName());
        product.setDescription(changedProduct.getDescription());
        product.setCurrency(changedProduct.getCurrency());
        product.setRegularPrice(changedProduct.getRegularPrice());
        return productRepository.save(product);

    }
}
