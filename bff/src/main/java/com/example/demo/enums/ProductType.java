package com.example.demo.enums;

public enum ProductType {
    ELECTRONIC("Electronic"),
    APPLIANCE("Appliance"),
    FURNITURE("Furniture");

    private final String description;

    ProductType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
