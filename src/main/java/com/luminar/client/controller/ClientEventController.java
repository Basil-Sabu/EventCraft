package com.luminar.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luminar.entity.Event;
import com.luminar.service.EventService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/client")
public class ClientEventController {

    @Autowired
    private EventService eventService;

    // ===============================
    // MY EVENTS (LIST)
    // ===============================
    @GetMapping("/events")
    public String myEvents(HttpSession session, Model model) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/";
        }

        List<Event> events =
                eventService.getEventsByUserId(userId);

        model.addAttribute("events", events);

        return "client/eventVED";
    }

    // ===============================
    // EVENT DETAILS (VIEW)
    // ===============================
    @GetMapping("/event/details")
    public String viewEventDetails(
            @RequestParam Long eventId,
            HttpSession session,
            Model model) {

        Long loggedUserId =
                (Long) session.getAttribute("userId");

        if (loggedUserId == null) {
            return "redirect:/";
        }

        Event event = eventService.getEventById(eventId);

        // 🔐 OWNERSHIP CHECK
        if (!event.getClient().getUserId().equals(loggedUserId)) {
            return "redirect:/client/dashboard";
        }

        model.addAttribute("event", event);

        return "client/eventDetails";
    }
}
