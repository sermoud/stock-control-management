package com.example.scm.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementRequest {

    private Long productId;
    private int quantity;

    private BigDecimal transactionValue;
}