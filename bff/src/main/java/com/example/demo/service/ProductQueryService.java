package com.example.demo.service;

import com.example.demo.dto.ProductProfitDTO;
import com.example.demo.dto.ProductStockInfoDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.StockChange;
import com.example.demo.enums.ChangeType;
import com.example.demo.enums.ProductType;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.StockChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final StockChangeRepository stockChangeRepository;

    @Autowired
    public ProductQueryService(ProductRepository productRepository, StockChangeRepository stockChangeRepository) {
        this.productRepository = productRepository;
        this.stockChangeRepository = stockChangeRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductStockInfoDTO> getProductsByTypeWithStockInfo(ProductType productType) {
        List<Product> products = productRepository.findByProductType(productType);

        return products.stream().map(product -> {
            List<StockChange> exits = stockChangeRepository.findByProductAndChangeType(product, ChangeType.EXIT);
            int totalQuantitySold = exits.stream()
                    .mapToInt(StockChange::getMovedQuantity)
                    .sum();
            return new ProductStockInfoDTO(
                    product.getCode(),
                    product.getDescription(),
                    totalQuantitySold,
                    product.getQuantityInStock());
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductProfitDTO> getProductsProfitability() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            List<StockChange> exits = stockChangeRepository.findByProductAndChangeType(product, ChangeType.EXIT);

            int totalQuantitySold = exits.stream()
                    .mapToInt(StockChange::getMovedQuantity)
                    .sum();

            BigDecimal totalSaleValueFromExits = exits.stream()
                    .map(StockChange::getSaleValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalSupplierCostForSoldItems = BigDecimal.valueOf(product.getSupplierValue())
                    .multiply(BigDecimal.valueOf(totalQuantitySold));

            BigDecimal totalProfit = totalSaleValueFromExits.subtract(totalSupplierCostForSoldItems);

            return new ProductProfitDTO(
                    product.getCode(),
                    product.getDescription(),
                    totalQuantitySold,
                    totalSaleValueFromExits,
                    totalSupplierCostForSoldItems, 
                    totalProfit);
        }).collect(Collectors.toList());
    }
}