package com.qxy.model.enums;

public enum ProductStatus {
    ACTIVE("active", "可用"),
    INACTIVE("inactive", "不可用");

    private final String code;
    private final String description;

    ProductStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}