package com.example.scm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.scm.dto.ProductProfitDTO;
import com.example.scm.dto.ProductStockInfoDTO;
import com.example.scm.entity.Product;
import com.example.scm.entity.StockChange;
import com.example.scm.enums.ChangeType;
import com.example.scm.enums.ProductType;
import com.example.scm.repository.ProductRepository;
import com.example.scm.repository.StockChangeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final StockChangeRepository stockChangeRepository;

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
                    totalProfit,
                    totalSaleValueFromExits,
                    totalSupplierCostForSoldItems);
        }).collect(Collectors.toList());
    }
}