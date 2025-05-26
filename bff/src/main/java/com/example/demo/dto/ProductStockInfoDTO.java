package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockInfoDTO {
    private String productCode;
    private String productDescription;
    private int quantitySold;
    private int quantityAvailable;
}