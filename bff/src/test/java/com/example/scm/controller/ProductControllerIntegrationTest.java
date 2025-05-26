package com.example.scm.controller;

import com.example.scm.entity.Product;
import com.example.scm.enums.ProductType;
import com.example.scm.repository.ProductRepository;
import com.example.scm.repository.StockChangeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional 
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockChangeRepository stockChangeRepository;

    @Autowired
    private ObjectMapper objectMapper; 

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        stockChangeRepository.deleteAll();
        productRepository.deleteAll(); 

        product1 = new Product(null, "PROD001", "Laptop X", ProductType.ELECTRONIC, 1200.00, 10);
        product2 = new Product(null, "PROD002", "Office Chair Y", ProductType.FURNITURE, 150.00, 25);

        product1 = productRepository.save(product1);
        product2 = productRepository.save(product2);
    }

    @Test
    void getAll_shouldReturnListOfProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code", is(product1.getCode())))
                .andExpect(jsonPath("$[1].code", is(product2.getCode())));
    }

    @Test
    void getById_whenProductExists_shouldReturnProduct() throws Exception {
        mockMvc.perform(get("/api/products/" + product1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$.code", is(product1.getCode())))
                .andExpect(jsonPath("$.description", is(product1.getDescription())));
    }

    @Test
    void getById_whenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        long nonExistentId = 999L;
        mockMvc.perform(get("/api/products/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldCreateProductAndReturnIt() throws Exception {
        Product newProduct = new Product(null, "PROD003", "New Keyboard", ProductType.ELECTRONIC, 75.00, 50);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.code", is("PROD003")))
                .andExpect(jsonPath("$.description", is("New Keyboard")))
                .andExpect(jsonPath("$.quantityInStock", is(50)));

        List<Product> products = productRepository.findAll();
        assertTrue(products.stream().anyMatch(p -> "PROD003".equals(p.getCode())));
    }

     @Test
    void create_withQuantityInStockInRequest_shouldBeHandledByService() throws Exception {
        Product newProductWithStockInRequest = new Product(null, "PROD004", "Mouse Z", ProductType.ELECTRONIC, 25.00, 100);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProductWithStockInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("PROD004")))
                .andExpect(jsonPath("$.quantityInStock", is(100)));
    }


    @Test
    void update_whenProductExists_shouldUpdateAndReturnProduct() throws Exception {
        Product updatedInfo = new Product(null, product1.getCode(), "Laptop X - Updated", ProductType.ELECTRONIC, 1250.00, 5);

        mockMvc.perform(put("/api/products/" + product1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(product1.getId().intValue())))
                .andExpect(jsonPath("$.description", is("Laptop X - Updated")))
                .andExpect(jsonPath("$.supplierValue", is(1250.00)));
        Product fromDb = productRepository.findById(product1.getId()).orElseThrow();
        assertTrue(fromDb.getDescription().equals("Laptop X - Updated"));
        assertTrue(fromDb.getSupplierValue().equals(1250.00));
    }

    @Test
    void update_whenProductDoesNotExist_shouldReturnNotFound() throws Exception {
        long nonExistentId = 999L;
        Product updatedInfo = new Product(null, "NONEXIST", "Non Existent Product", ProductType.APPLIANCE, 10.00, 1);

        mockMvc.perform(put("/api/products/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_whenProductExists_shouldDeleteProductAndReturnNoContent() throws Exception {
        assertTrue(productRepository.findById(product1.getId()).isPresent());

        mockMvc.perform(delete("/api/products/" + product1.getId()))
                .andExpect(status().isNoContent());

        assertFalse(productRepository.findById(product1.getId()).isPresent());
    }

    @Test
    void delete_whenProductDoesNotExist_shouldReturnNoContent() throws Exception {
        long nonExistentId = 999L;
        assertFalse(productRepository.findById(nonExistentId).isPresent());

        mockMvc.perform(delete("/api/products/" + nonExistentId))
                .andExpect(status().isNoContent()); 
    }
}