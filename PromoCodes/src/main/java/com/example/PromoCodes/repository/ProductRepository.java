package com.example.PromoCodes.repository;

import com.example.PromoCodes.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

}
