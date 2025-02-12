package com.example.order_system.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.order_system.model.Inventory;
import com.example.order_system.model.Order;
import com.example.order_system.model.OrderEvent;
import com.example.order_system.model.OrderItem;
import com.example.order_system.repository.InventoryRepository;
import com.example.order_system.repository.OrderRepository;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private EventQueueService eventQueueService;

    @Transactional
    public Order createOrder(Order order) {
        order.setOrderId(UUID.randomUUID().toString());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            Inventory inventory = inventoryRepository.getInventory(item.getProductId());
            if (inventory == null) {
                throw new IllegalArgumentException("Product not found: " + item.getProductId());
            }
            if (inventory.getStockLevel() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + item.getProductId());
            }
            item.setUnitPrice(inventory.getUnitPrice());
            totalAmount = totalAmount.add(inventory.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        order.setTotalAmount(totalAmount);
        orderRepository.saveOrder(order);
        for (OrderItem item : order.getItems()) {
            inventoryRepository.updateStock(item.getProductId(), item.getQuantity());
        }

        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setOrderId(order.getOrderId());
        event.setEventType("ORDER_CREATED");
        event.setPayload(createJsonPayload(order));
        event.setCreatedAt(LocalDateTime.now());
        event.setStatus("PENDING");
        eventQueueService.queueEvent(event);
        return order;
    }

    private String createJsonPayload(Order order) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("orderId", order.getOrderId());
        builder.add("customerId", order.getCustomerId());
        builder.add("status", order.getStatus());
        builder.add("totalAmount", order.getTotalAmount());

        JsonArrayBuilder itemsBuilder = Json.createArrayBuilder();
        for (OrderItem item : order.getItems()) {
            JsonObjectBuilder itemBuilder = Json.createObjectBuilder();
            itemBuilder.add("productId", item.getProductId());
            itemBuilder.add("quantity", item.getQuantity());
            itemBuilder.add("unitPrice", item.getUnitPrice());
            itemsBuilder.add(itemBuilder);
        }
        builder.add("items", itemsBuilder);

        return builder.build().toString();
    }

    public Order getOrder(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        return order;
    }
}
