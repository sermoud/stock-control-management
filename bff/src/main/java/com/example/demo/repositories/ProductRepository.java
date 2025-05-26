package com.example.demo.repositories;

import com.example.demo.entities.Product;
import com.example.demo.enums.ProductType;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductType(ProductType productType);

    Optional<String> findByCode(String newProductCode);
}