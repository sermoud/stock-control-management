package com.example.demo.repositories;

import com.example.demo.entities.Product;
import com.example.demo.entities.StockChange;
import com.example.demo.enums.ChangeType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StockChangeRepository extends JpaRepository<StockChange, Long> {

    List<StockChange> findByProductAndChangeType(Product product, ChangeType exit);
}
