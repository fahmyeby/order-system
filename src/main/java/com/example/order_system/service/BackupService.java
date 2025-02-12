package com.example.order_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.order_system.model.OrderEvent;
import com.example.order_system.repository.EventRepository;

@Service
public class BackupService {
    @Autowired
    private EventRepository eventRepository;

    public void backupEvent(OrderEvent event){
        try {
            eventRepository.saveEvent(event);
        } catch (Exception e) {
            System.err.println("Failed to backup event: " + e.getMessage());
        }
    }
}
