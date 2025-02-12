package com.example.order_system.repository;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.example.order_system.model.OrderEvent;

@Repository
public class EventRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * MongoDB query:
     * db.events.insert({
     * eventId: "eventId",
     * orderId: "orderId",
     * eventType: "eventType",
     * payload: "payload",
     * createdAt: ISODate("2024-02-12T00:00:00Z"),
     * status: "status"
     * })
     */
    public void saveEvent(OrderEvent event) {
        Document eventDoc = new Document();
        eventDoc.put("eventId", event.getEventId());
        eventDoc.put("orderId", event.getOrderId());
        eventDoc.put("eventType", event.getEventType());
        eventDoc.put("payload", event.getPayload());
        eventDoc.put("createdAt", event.getCreatedAt());
        eventDoc.put("status", event.getStatus());

        mongoTemplate.getCollection("events").insertOne(eventDoc);
    }

    public List<OrderEvent> getEventsByOrderId(String orderId) {
        Document query = new Document("orderId", orderId);
        List<Document> documents = mongoTemplate.getCollection("events").find(query).into(new ArrayList<>());
        List<OrderEvent> events = new ArrayList<>();
        for (Document doc : documents) {
            OrderEvent event = new OrderEvent();
            event.setEventId(doc.getString("eventId"));
            event.setOrderId(doc.getString("orderId"));
            event.setEventType(doc.getString("eventType"));
            event.setPayload(doc.getString("payload"));
            event.setCreatedAt(doc.getDate("createdAt").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            event.setStatus(doc.getString("status"));
            events.add(event);
        }
        return events;
    }
}
