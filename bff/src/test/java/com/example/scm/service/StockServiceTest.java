package com.example.scm.service;

import com.example.scm.dto.StockMovementRequest;
import com.example.scm.entity.Product;
import com.example.scm.entity.StockChange;
import com.example.scm.enums.ChangeType;
import com.example.scm.enums.ProductType;
import com.example.scm.exception.InsufficientStockException;
import com.example.scm.exception.ResourceNotFoundException;
import com.example.scm.repository.ProductRepository;
import com.example.scm.repository.StockChangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockChangeRepository stockChangeRepository;

    @InjectMocks
    private StockService stockService;

    private Product product1;
    private StockMovementRequest entryRequest;
    private StockMovementRequest exitRequest;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "PROD001", "Laptop", ProductType.ELECTRONIC, 1000.00, 10);
        entryRequest = new StockMovementRequest(1L, 5, new BigDecimal("5000.00"));
        exitRequest = new StockMovementRequest(1L, 3, new BigDecimal("3300.00"));
    }

    @Test
    @DisplayName("recordStockEntry - Should record entry and update product stock successfully")
    void recordStockEntry_success() {
        when(productRepository.findById(entryRequest.getProductId())).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);
        when(stockChangeRepository.save(any(StockChange.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = stockService.recordStockEntry(entryRequest);

        assertNotNull(updatedProduct);
        assertEquals(15, updatedProduct.getQuantityInStock());

        ArgumentCaptor<StockChange> stockChangeCaptor = ArgumentCaptor.forClass(StockChange.class);
        verify(stockChangeRepository, times(1)).save(stockChangeCaptor.capture());
        StockChange savedStockChange = stockChangeCaptor.getValue();

        assertEquals(product1, savedStockChange.getProduct());
        assertEquals(ChangeType.ENTRY, savedStockChange.getChangeType());
        assertEquals(entryRequest.getQuantity(), savedStockChange.getMovedQuantity());
        assertEquals(entryRequest.getTransactionValue(), savedStockChange.getSaleValue());
        assertNotNull(savedStockChange.getSaleDate());

        verify(productRepository, times(1)).findById(entryRequest.getProductId());
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    @DisplayName("recordStockEntry - Should throw ResourceNotFoundException when product not found")
    void recordStockEntry_productNotFound() {
        when(productRepository.findById(entryRequest.getProductId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            stockService.recordStockEntry(entryRequest);
        });

        assertEquals("Produto não encontrado com ID: " + entryRequest.getProductId(), exception.getMessage());
        verify(stockChangeRepository, never()).save(any(StockChange.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("recordStockExit - Should record exit and update product stock successfully")
    void recordStockExit_success() {
        when(productRepository.findById(exitRequest.getProductId())).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);
        when(stockChangeRepository.save(any(StockChange.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = stockService.recordStockExit(exitRequest);

        assertNotNull(updatedProduct);
        assertEquals(7, updatedProduct.getQuantityInStock());

        ArgumentCaptor<StockChange> stockChangeCaptor = ArgumentCaptor.forClass(StockChange.class);
        verify(stockChangeRepository, times(1)).save(stockChangeCaptor.capture());
        StockChange savedStockChange = stockChangeCaptor.getValue();

        assertEquals(product1, savedStockChange.getProduct());
        assertEquals(ChangeType.EXIT, savedStockChange.getChangeType());
        assertEquals(exitRequest.getQuantity(), savedStockChange.getMovedQuantity());
        assertEquals(exitRequest.getTransactionValue(), savedStockChange.getSaleValue());
        assertNotNull(savedStockChange.getSaleDate());

        verify(productRepository, times(1)).findById(exitRequest.getProductId());
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    @DisplayName("recordStockExit - Should throw ResourceNotFoundException when product not found")
    void recordStockExit_productNotFound() {
        when(productRepository.findById(exitRequest.getProductId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            stockService.recordStockExit(exitRequest);
        });

        assertEquals("Produto não encontrado com ID: " + exitRequest.getProductId(), exception.getMessage());
        verify(stockChangeRepository, never()).save(any(StockChange.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("recordStockExit - Should throw InsufficientStockException when stock is not enough")
    void recordStockExit_insufficientStock() {
        product1.setQuantityInStock(2);
        StockMovementRequest largeExitRequest = new StockMovementRequest(1L, 5, new BigDecimal("5000.00"));

        when(productRepository.findById(largeExitRequest.getProductId())).thenReturn(Optional.of(product1));

        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () -> {
            stockService.recordStockExit(largeExitRequest);
        });

        String expectedMessage = "Quantidade insuficiente em estoque para o produto: " + product1.getDescription() +
                                 ". Disponível: " + product1.getQuantityInStock() + ", Requisitado: " + largeExitRequest.getQuantity();
        assertEquals(expectedMessage, exception.getMessage());

        verify(stockChangeRepository, never()).save(any(StockChange.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("getStockMovementHistory - Should return list of stock changes")
    void getStockMovementHistory_returnsHistory() {
        StockChange change1 = new StockChange(1L, product1, ChangeType.ENTRY, new BigDecimal("100.00"), LocalDateTime.now(), 10);
        StockChange change2 = new StockChange(2L, product1, ChangeType.EXIT, new BigDecimal("50.00"), LocalDateTime.now(), 5);
        List<StockChange> expectedHistory = Arrays.asList(change1, change2);

        when(stockChangeRepository.findAll()).thenReturn(expectedHistory);

        List<StockChange> actualHistory = stockService.getStockMovementHistory();

        assertNotNull(actualHistory);
        assertEquals(2, actualHistory.size());
        assertEquals(expectedHistory, actualHistory);
        verify(stockChangeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getStockMovementHistory - Should return empty list when no history")
    void getStockMovementHistory_returnsEmptyList() {
        when(stockChangeRepository.findAll()).thenReturn(Collections.emptyList());

        List<StockChange> actualHistory = stockService.getStockMovementHistory();

        assertNotNull(actualHistory);
        assertTrue(actualHistory.isEmpty());
        verify(stockChangeRepository, times(1)).findAll();
    }
}