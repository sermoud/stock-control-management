package com.example.demo.enums;

public enum ChangeType {
    ENTRY("Entry"),
    EXIT("Exit");

    private final String description;

    ChangeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
