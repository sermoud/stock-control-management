package com.example.scm;

import com.example.scm.dto.ProductProfitDTO;
import com.example.scm.dto.ProductStockInfoDTO;
import com.example.scm.entity.Product;
import com.example.scm.entity.StockChange;
import com.example.scm.enums.ChangeType;
import com.example.scm.enums.ProductType;
import com.example.scm.repository.ProductRepository;
import com.example.scm.repository.StockChangeRepository;
import com.example.scm.service.ProductQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductQueryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockChangeRepository stockChangeRepository;

    @InjectMocks
    private ProductQueryService productQueryService;

    private Product product1;
    private Product product2;
    private StockChange exit1Product1;
    private StockChange exit2Product1;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "PROD001", "Laptop", ProductType.ELECTRONIC, 1000.00, 10);
        product2 = new Product(2L, "PROD002", "Mouse", ProductType.ELECTRONIC, 50.00, 20);

        exit1Product1 = new StockChange(1L, product1, ChangeType.EXIT, new BigDecimal("1200.0"), LocalDateTime.now(), 2);
        exit2Product1 = new StockChange(2L, product1, ChangeType.EXIT, new BigDecimal("1250.0"), LocalDateTime.now(), 1);
    }

    @Test
    @DisplayName("getProductsByTypeWithStockInfo - Should return DTOs with correct stock info")
    void getProductsByTypeWithStockInfo_shouldReturnDTOsWithStockInfo() {
        when(productRepository.findByProductType(ProductType.ELECTRONIC)).thenReturn(Arrays.asList(product1, product2));
        when(stockChangeRepository.findByProductAndChangeType(product1, ChangeType.EXIT)).thenReturn(Arrays.asList(exit1Product1, exit2Product1));
        when(stockChangeRepository.findByProductAndChangeType(product2, ChangeType.EXIT)).thenReturn(Collections.emptyList());

        List<ProductStockInfoDTO> result = productQueryService.getProductsByTypeWithStockInfo(ProductType.ELECTRONIC);

        assertNotNull(result);
        assertEquals(2, result.size());

        ProductStockInfoDTO dto1 = result.stream().filter(dto -> "PROD001".equals(dto.getProductCode())).findFirst().orElse(null);
        assertNotNull(dto1);
        assertEquals("Laptop", dto1.getProductDescription());
        assertEquals(3, dto1.getQuantitySold()); 
        assertEquals(10, dto1.getQuantityAvailable());

        ProductStockInfoDTO dto2 = result.stream().filter(dto -> "PROD002".equals(dto.getProductCode())).findFirst().orElse(null);
        assertNotNull(dto2);
        assertEquals("Mouse", dto2.getProductDescription());
        assertEquals(0, dto2.getQuantitySold());
        assertEquals(20, dto2.getQuantityAvailable());
    }

    @Test
    @DisplayName("getProductsByTypeWithStockInfo - Should return empty list when no products of type found")
    void getProductsByTypeWithStockInfo_shouldReturnEmptyListWhenNoProductsFound() {
        when(productRepository.findByProductType(ProductType.FURNITURE)).thenReturn(Collections.emptyList());

        List<ProductStockInfoDTO> result = productQueryService.getProductsByTypeWithStockInfo(ProductType.FURNITURE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getProductsByTypeWithStockInfo - Should return DTOs with zero sold when no exits")
    void getProductsByTypeWithStockInfo_shouldReturnZeroSoldWhenNoExits() {
        Product productNoExits = new Product(3L, "PROD003", "Keyboard", ProductType.ELECTRONIC, 75.00, 5);
        when(productRepository.findByProductType(ProductType.ELECTRONIC)).thenReturn(Collections.singletonList(productNoExits));
        when(stockChangeRepository.findByProductAndChangeType(productNoExits, ChangeType.EXIT)).thenReturn(Collections.emptyList());

        List<ProductStockInfoDTO> result = productQueryService.getProductsByTypeWithStockInfo(ProductType.ELECTRONIC);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getQuantitySold());
        assertEquals(5, result.get(0).getQuantityAvailable());
    }


    @Test
    @DisplayName("getProductsProfitability - Should return DTOs with correct profitability info")
    void getProductsProfitability_shouldReturnDTOsWithProfitabilityInfo() {
        Product product3 = new Product(3L, "PROD003", "Monitor", ProductType.ELECTRONIC, 200.00, 5);
        StockChange exitProduct3 = new StockChange(3L, product3, ChangeType.EXIT, new BigDecimal("300.00"), LocalDateTime.now(), 1);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product3));
        when(stockChangeRepository.findByProductAndChangeType(product1, ChangeType.EXIT)).thenReturn(Arrays.asList(exit1Product1, exit2Product1));
        when(stockChangeRepository.findByProductAndChangeType(product3, ChangeType.EXIT)).thenReturn(Collections.singletonList(exitProduct3));

        List<ProductProfitDTO> result = productQueryService.getProductsProfitability();

        assertNotNull(result);
        assertEquals(2, result.size());

        ProductProfitDTO dto1 = result.stream().filter(dto -> "PROD001".equals(dto.getProductCode())).findFirst().orElse(null);
        assertNotNull(dto1);
        assertEquals("Laptop", dto1.getProductDescription());
        assertEquals(3, dto1.getTotalQuantitySold()); 
        assertTrue(new BigDecimal("2450.0").compareTo(dto1.getTotalSaleValue()) == 0);
        assertTrue(new BigDecimal("3000.0").compareTo(dto1.getTotalSupplierCost()) == 0);
        assertTrue(new BigDecimal("-550.0").compareTo(dto1.getTotalProfit()) == 0);


        ProductProfitDTO dto3 = result.stream().filter(dto -> "PROD003".equals(dto.getProductCode())).findFirst().orElse(null);
        assertNotNull(dto3);
        assertEquals("Monitor", dto3.getProductDescription());
        assertEquals(1, dto3.getTotalQuantitySold());
        assertTrue(new BigDecimal("300.00").compareTo(dto3.getTotalSaleValue()) == 0);
        assertTrue(new BigDecimal("200.0").compareTo(dto3.getTotalSupplierCost()) == 0);
        assertTrue(new BigDecimal("100.00").compareTo(dto3.getTotalProfit()) == 0);
    }

    @Test
    @DisplayName("getProductsProfitability - Should return empty list when no products found")
    void getProductsProfitability_shouldReturnEmptyListWhenNoProductsFound() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductProfitDTO> result = productQueryService.getProductsProfitability();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getProductsProfitability - Should handle products with no exit movements")
    void getProductsProfitability_shouldHandleProductsWithNoExitMovements() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product2));
        when(stockChangeRepository.findByProductAndChangeType(product2, ChangeType.EXIT)).thenReturn(Collections.emptyList());

        List<ProductProfitDTO> result = productQueryService.getProductsProfitability();

        assertNotNull(result);
        assertEquals(1, result.size());

        ProductProfitDTO dto2 = result.get(0);
        assertEquals("PROD002", dto2.getProductCode());
        assertEquals("Mouse", dto2.getProductDescription());
        assertEquals(0, dto2.getTotalQuantitySold());
        assertTrue(BigDecimal.ZERO.compareTo(dto2.getTotalSaleValue()) == 0);
        assertTrue(BigDecimal.ZERO.compareTo(dto2.getTotalSupplierCost()) == 0);
        assertTrue(BigDecimal.ZERO.compareTo(dto2.getTotalProfit()) == 0);
    }
}