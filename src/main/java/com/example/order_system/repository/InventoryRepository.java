package com.example.order_system.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order_system.model.Inventory;

@Repository
public class InventoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Inventory getInventory(String productId) {
        String sql = """
                select * from inventory where product_id = ?
                """;
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] { productId }, (rs, rowNum) -> {
                Inventory inventory = new Inventory();
                inventory.setProductId(rs.getString("product_id"));
                inventory.setProductName(rs.getString("product_name"));
                inventory.setStockLevel(rs.getInt("stock_level"));
                inventory.setUnitPrice(rs.getBigDecimal("unit_price"));
                return inventory;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateStock(String productId, Integer quantity) {
        String sql = """
                update inventory set stock_level = stock_level - ?
                where product_id = ?
                and stock_level >= ?
                """;
        Integer updated = jdbcTemplate.update(sql, quantity, productId, quantity);
        if (updated == 0) {
            throw new IllegalStateException("Stocks insufficient for " + productId);
        }
    }
}
