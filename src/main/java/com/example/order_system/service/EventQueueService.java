package com.example.order_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.order_system.model.OrderEvent;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;

@Service
public class EventQueueService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private BackupService backupService;

    private static final String eventQKey = "order:events:queue";

    public void queueEvent(OrderEvent event) {
        String eventJson = createJsonString(event);
        redisTemplate.opsForList().rightPush(eventQKey, eventJson);
        backupService.backupEvent(event);
    }

    private String createJsonString(OrderEvent event) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("eventId", event.getEventId());
        builder.add("orderId", event.getOrderId());
        builder.add("eventType", event.getEventType());
        builder.add("payload", event.getPayload());
        builder.add("createdAt", event.getCreatedAt().toString());
        builder.add("status", event.getStatus());
        return builder.build().toString();
    }
}
