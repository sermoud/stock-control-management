package com.example.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.scm.dto.ProductProfitDTO;
import com.example.scm.dto.ProductStockInfoDTO;
import com.example.scm.enums.ProductType;
import com.example.scm.service.ProductQueryService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductQueryApiController {

    private final ProductQueryService productQueryService;

    @Autowired
    public ProductQueryApiController(ProductQueryService productQueryService) {
        this.productQueryService = productQueryService;
    }

    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<ProductStockInfoDTO>> getProductsByType(@PathVariable ProductType type) {
        List<ProductStockInfoDTO> products = productQueryService.getProductsByTypeWithStockInfo(type);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/profitability")
    public ResponseEntity<List<ProductProfitDTO>> getProductProfitability() {
        List<ProductProfitDTO> productProfits = productQueryService.getProductsProfitability();
        return ResponseEntity.ok(productProfits);
    }
}