package com.example.order_system.model;

import java.time.LocalDateTime;

public class OrderEvent {
    private String eventId;
    private String orderId;
    private String eventType;
    private String payload;
    private LocalDateTime createdAt;
    private String status;

    public OrderEvent() {
    }

    public OrderEvent(String eventId, String orderId, String eventType, String payload, LocalDateTime createdAt,
            String status) {
        this.eventId = eventId;
        this.orderId = orderId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = createdAt;
        this.status = status;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
