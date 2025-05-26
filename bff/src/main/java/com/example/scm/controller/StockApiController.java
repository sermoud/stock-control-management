package com.example.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.scm.dto.StockMovementRequest;
import com.example.scm.entity.Product;
import com.example.scm.entity.StockChange;
import com.example.scm.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockApiController {

    private final StockService stockService;

    @Autowired
    public StockApiController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/entry")
    public ResponseEntity<Product> recordEntry(@RequestBody StockMovementRequest request) {
        Product updatedProduct = stockService.recordStockEntry(request);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/exit")
    public ResponseEntity<Product> recordExit(@RequestBody StockMovementRequest request) {
        Product updatedProduct = stockService.recordStockExit(request);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/movements-history")
    public ResponseEntity<List<StockChange>> getStockMovementsHistory() {
        List<StockChange> history = stockService.getStockMovementHistory();
        return ResponseEntity.ok(history);
    }
}