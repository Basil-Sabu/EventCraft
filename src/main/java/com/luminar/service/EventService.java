package com.luminar.service;

import java.util.List;

import com.luminar.entity.Event;

public interface EventService {

    // Save new event
    Event saveEvent(Event event);

    // List all events
    List<Event> getAllEvents();

    // Get single event by ID
    Event getEventById(Long eventId);

    // Delete (hard delete for now)
    void deleteEvent(Long eventId);

    // List events by client
    List<Event> getEventsByClient(Long clientId);
    
    // ✅ NEW — List events by logged-in user (client)
    List<Event> getEventsByUserId(Long userId);
}
