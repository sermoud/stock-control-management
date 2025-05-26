package com.example.scm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scm.entity.Product;
import com.example.scm.enums.ProductType;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductType(ProductType productType);

    Optional<String> findByCode(String newProductCode);
}