package com.example.demo;

import com.example.demo.entities.Product;
import com.example.demo.entities.StockChange;
import com.example.demo.enums.ChangeType;
import com.example.demo.enums.ProductType;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.StockChangeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepo, StockChangeRepository stockChangeRepo) {
        return args -> {
            Product laptop = new Product();
            laptop.setCode("PROD001");
            laptop.setDescription("Dell XPS 13");
            laptop.setProductType(ProductType.ELECTRONIC);
            laptop.setSupplierValue(5000.00);
            laptop.setQuantityInStock(10);
            productRepo.save(laptop);

            Product fridge = new Product();
            fridge.setCode("PROD002");
            fridge.setDescription("LG Smart Fridge");
            fridge.setProductType(ProductType.APPLIANCE);
            fridge.setSupplierValue(3500.00);
            fridge.setQuantityInStock(5);
            productRepo.save(fridge);

            Product chair = new Product();
            chair.setCode("PROD003");
            chair.setDescription("Office Chair");
            chair.setProductType(ProductType.FURNITURE);
            chair.setSupplierValue(400.00);
            chair.setQuantityInStock(15);
            productRepo.save(chair);

            stockChangeRepo.save(new StockChange(null, laptop, ChangeType.ENTRY, new BigDecimal("7000.00"), LocalDateTime.now(), 2));
            stockChangeRepo.save(new StockChange(null, laptop, ChangeType.EXIT, new BigDecimal("7500.00"), LocalDateTime.now(), 1));
            stockChangeRepo.save(new StockChange(null, fridge, ChangeType.ENTRY, new BigDecimal("5000.00"), LocalDateTime.now(), 1));
            stockChangeRepo.save(new StockChange(null, chair, ChangeType.EXIT, new BigDecimal("600.00"), LocalDateTime.now(), 3));
        };
    }
}
