package com.example.scm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockInfoDTO {
    private String productCode;
    private String productDescription;
    private Integer quantitySold;
    private Integer quantityAvailable;
}