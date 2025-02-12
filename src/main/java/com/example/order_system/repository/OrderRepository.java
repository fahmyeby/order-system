package com.example.order_system.repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order_system.model.Order;
import com.example.order_system.model.OrderItem;

@Repository
public class OrderRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveOrder(Order order) {
        String sql = """
                insert into orders (order_id, customer_id, total_amount, status, created_at)
                values (?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                order.getOrderId(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus(),
                Timestamp.valueOf(order.getCreatedAt()));

        String itemSql = """
                insert into order_items (order_id, product_id, quantity, unit_price)
                values (?, ?, ?, ?)
                """;

        for (OrderItem item : order.getItems()) {
            jdbcTemplate.update(itemSql, order.getOrderId(),
                    item.getProductId(),
                    item.getQuantity(),
                    item.getUnitPrice());
        }
    }

    public Order findById(String orderId) {
        String sql = """
                select o.*, oi.product_id, oi.quantity, oi.unit_price
                from orders o
                left join order_items oi on o.order_id = oi.order_id
                where o.order_id = ?
                """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, orderId);
        if (rows.isEmpty()) {
            return null;
        }
        Order order = new Order();
        Map<String, Object> firstRow = rows.get(0);
        order.setOrderId((String) firstRow.get("order_id"));
        order.setCustomerId((String) firstRow.get("customer_id"));
        order.setTotalAmount((BigDecimal) firstRow.get("total_amount"));
        order.setStatus((String) firstRow.get("status"));
        Timestamp timestamp = (Timestamp) firstRow.get("created_at");
        if (timestamp != null){
            order.setCreatedAt(timestamp.toLocalDateTime());
        }
        List<OrderItem> items = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            if (row.get("product_id") != null) {
                OrderItem item = new OrderItem();
                item.setProductId((String) row.get("product_id"));
                item.setQuantity((Integer) row.get("quantity"));
                item.setUnitPrice((BigDecimal) row.get("unit_price"));
                items.add(item);
            }
        }
        order.setItems(items);
        return order;
    }
}
