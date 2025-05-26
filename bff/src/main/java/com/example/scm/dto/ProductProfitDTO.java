package com.example.scm.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductProfitDTO {
    private String productCode;
    private String productDescription;
    private int totalQuantitySold;
    private BigDecimal totalProfit;
    private BigDecimal totalSaleValue; 
    private BigDecimal totalSupplierCost;

}