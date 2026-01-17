package com.luminar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.entity.Event;
import com.luminar.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event saveEvent(Event event) {

        Long clientId = event.getClient().getClientId();

        // ADD case
        if (event.getEventId() == null) {

            boolean exists = eventRepository
                .existsByClientClientIdAndEventNameAndEventDate(
                    clientId,
                    event.getEventName(),
                    event.getEventDate()
                );

            if (exists) {
                throw new RuntimeException(
                    "An event with the same name already exists for this client on the selected date"
                );
            }

        }
        // UPDATE case
        else {

            boolean exists = eventRepository
                .existsByClientClientIdAndEventNameAndEventDateAndEventIdNot(
                    clientId,
                    event.getEventName(),
                    event.getEventDate(),
                    event.getEventId()
                );

            if (exists) {
                throw new RuntimeException(
                    "Another event with the same name already exists for this client on the selected date"
                );
            }
        }

        return eventRepository.save(event);
    }



    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException("Event not found with id: " + eventId));
    }

    @Override
    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    @Override
    // to fetch events based on client for other modules
    public List<Event> getEventsByClient(Long clientId) {
        return eventRepository.findByClientClientId(clientId);
    }
    @Override
    //for client side view
    public List<Event> getEventsByUserId(Long userId) {
        return eventRepository.findByClientUserId(userId);
    }

}
