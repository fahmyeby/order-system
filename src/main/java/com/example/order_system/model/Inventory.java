package com.example.order_system.model;

import java.math.BigDecimal;

public class Inventory {
    private String productId;
    private String productName;
    private int stockLevel;
    private BigDecimal unitPrice;

    public Inventory() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Inventory(String productId, String productName, int stockLevel, BigDecimal unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.stockLevel = stockLevel;
        this.unitPrice = unitPrice;
    }

}
