package com.example.scm.service;

import org.springframework.stereotype.Service;

import com.example.scm.entity.Product;
import com.example.scm.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    public Product save(Product product) {
        return repository.save(product);
    }

    public Product update(Long id, Product productDetails) {
        Product product = repository.findById(id).orElseThrow();
        product.setCode(productDetails.getCode());
        product.setDescription(productDetails.getDescription());
        product.setProductType(productDetails.getProductType());
        product.setSupplierValue(productDetails.getSupplierValue());
        product.setQuantityInStock(productDetails.getQuantityInStock());
        return repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
