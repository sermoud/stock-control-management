package com.example.scm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.scm.entity.Product;
import com.example.scm.entity.StockChange;
import com.example.scm.enums.ChangeType;

public interface StockChangeRepository extends JpaRepository<StockChange, Long> {

    List<StockChange> findByProductAndChangeType(Product product, ChangeType exit);
}
