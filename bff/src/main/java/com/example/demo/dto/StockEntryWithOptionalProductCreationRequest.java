package com.example.demo.dto;

import com.example.demo.enums.ProductType;
import java.math.BigDecimal;

public class StockEntryWithOptionalProductCreationRequest {

    private Long productId;

    private int quantity;

    private BigDecimal transactionValue;

    private boolean createNewProduct;

    private String newProductCode;

    private String newProductDescription;

    private ProductType newProductType;

    private Double newProductSupplierValue;

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getTransactionValue() {
        return transactionValue;
    }
    public void setTransactionValue(BigDecimal transactionValue) {
        this.transactionValue = transactionValue;
    }
    public boolean isCreateNewProduct() {
        return createNewProduct;
    }
    public void setCreateNewProduct(boolean createNewProduct) {
        this.createNewProduct = createNewProduct;
    }
    public String getNewProductCode() {
        return newProductCode;
    }
    public void setNewProductCode(String newProductCode) {
        this.newProductCode = newProductCode;
    }
    public String getNewProductDescription() {
        return newProductDescription;
    }
    public void setNewProductDescription(String newProductDescription) {
        this.newProductDescription = newProductDescription;
    }
    public ProductType getNewProductType() {
        return newProductType;
    }
    public void setNewProductType(ProductType newProductType) {
        this.newProductType = newProductType;
    }
    public Double getNewProductSupplierValue() {
        return newProductSupplierValue;
    }
    public void setNewProductSupplierValue(Double newProductSupplierValue) {
        this.newProductSupplierValue = newProductSupplierValue;
    }
}