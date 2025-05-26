package com.example.demo.service;

import com.example.demo.dto.StockMovementRequest;
import com.example.demo.entities.Product;
import com.example.demo.entities.StockChange;
import com.example.demo.enums.ChangeType;
import com.example.demo.exception.InsufficientStockException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.StockChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockService {

    private final ProductRepository productRepository;
    private final StockChangeRepository stockChangeRepository;

    @Autowired
    public StockService(ProductRepository productRepository, StockChangeRepository stockChangeRepository) {
        this.productRepository = productRepository;
        this.stockChangeRepository = stockChangeRepository;
    }

    @Transactional
    public Product recordStockEntry(StockMovementRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + request.getProductId()));

        StockChange stockChange = new StockChange();
        stockChange.setProduct(product);
        stockChange.setChangeType(ChangeType.ENTRY);
        stockChange.setMovedQuantity(request.getQuantity());
        stockChange.setSaleValue(request.getTransactionValue());
        stockChange.setSaleDate(LocalDateTime.now());
        stockChangeRepository.save(stockChange);

        product.setQuantityInStock(product.getQuantityInStock() + request.getQuantity());
        return productRepository.save(product);
    }

    @Transactional
    public Product recordStockExit(StockMovementRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + request.getProductId()));

        if (product.getQuantityInStock() < request.getQuantity()) {
            throw new InsufficientStockException(
                    "Quantidade insuficiente em estoque para o produto: " + product.getDescription() +
                    ". Disponível: " + product.getQuantityInStock() + ", Requisitado: " + request.getQuantity()
            );
        }

        StockChange stockChange = new StockChange();
        stockChange.setProduct(product);
        stockChange.setChangeType(ChangeType.EXIT);
        stockChange.setMovedQuantity(request.getQuantity());
        stockChange.setSaleValue(request.getTransactionValue());
        stockChange.setSaleDate(LocalDateTime.now());
        stockChangeRepository.save(stockChange);

        product.setQuantityInStock(product.getQuantityInStock() - request.getQuantity());
        return productRepository.save(product);
    }

    public List<StockChange> getStockMovementHistory() {
        return stockChangeRepository.findAll();
    }


}