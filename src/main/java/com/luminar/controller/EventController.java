package com.luminar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luminar.entity.Client;
import com.luminar.entity.Event;
import com.luminar.service.ClientService;
import com.luminar.service.EventService;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ClientService clientService;

    // ===============================
    // ADD EVENT
    // ===============================
    @GetMapping("/events/add")
    public String showAddEventForm(Model model) {

        model.addAttribute("event", new Event());

        model.addAttribute("clients",
                clientService.getAllClients()); // you can later filter ACTIVE

        return "eventAdd";
    }

    // ===============================
    // SAVE EVENT
    // ===============================
    @PostMapping("/events/save")
    public String saveEvent(
            @ModelAttribute Event event,
            @RequestParam Long clientId,
            RedirectAttributes redirectAttributes) {

        try {
            Client client = clientService.getClientById(clientId);
            event.setClient(client);

            if (event.getEventId() == null) {
                eventService.saveEvent(event);
                redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Event added successfully"
                );
            } else {
                eventService.saveEvent(event);
                redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Event updated successfully"
                );
            }

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                e.getMessage()
            );
        }

        return "redirect:/events/add";
    }



    // ===============================
    // EVENT LIST
    // ===============================
    @GetMapping("/events/list")
    public String listEvents(Model model) {

        model.addAttribute("events", eventService.getAllEvents());

        return "eventVED";
    }

    // ===============================
    // EDIT EVENT
    // ===============================
    @GetMapping("/events/edit")
    public String editEvent(
            @RequestParam Long eventId,
            Model model) {

        Event event = eventService.getEventById(eventId);

        model.addAttribute("event", event);
        model.addAttribute("clients", clientService.getAllClients());

        return "eventAdd";
    }

    

    // ===============================
    // DELETE EVENT
    // ===============================
    @GetMapping("/events/delete")
    public String deleteEvent(@RequestParam Long eventId) {

        eventService.deleteEvent(eventId);

        return "redirect:/events/list";
    }
}
