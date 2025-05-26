package com.example.demo.controller;

import com.example.demo.dto.StockMovementRequest;
import com.example.demo.entities.Product;
import com.example.demo.entities.StockChange;
import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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