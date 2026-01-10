package com.luminar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luminar.entity.Event;
import com.luminar.entity.Guest;
import com.luminar.service.ClientService;
import com.luminar.service.EventService;
import com.luminar.service.GuestService;

@Controller
public class GuestController {

    @Autowired
    private GuestService guestService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EventService eventService;

    // =================================================
    // GUEST GROUP LIST PAGE (CLIENT + EVENT SELECTION)
    // =================================================
    @GetMapping("/guest-management")
    public String showGuestGroupPage(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long eventId,
            Model model) {

        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("guest", new Guest());

        // If client selected → load events
        if (clientId != null) {
            model.addAttribute(
                "events",
                eventService.getEventsByClient(clientId)
            );
            model.addAttribute("selectedClientId", clientId);
        }

        // If event selected → load guest groups
        if (eventId != null) {
            model.addAttribute(
                "guests",
                guestService.getGuestsByEvent(eventId)
            );

            model.addAttribute(
                "totalGuestCount",
                guestService.getTotalGuestCountByEvent(eventId)
            );

            model.addAttribute("selectedEventId", eventId);
        }

        return "guestManagement";
    }

    // =================================================
    // SAVE GUEST GROUP (ADD / UPDATE)
    // =================================================
    @PostMapping("/guest-management/save")
    public String saveGuest(
            @ModelAttribute Guest guest,
            @RequestParam Long eventId,
            RedirectAttributes redirectAttributes) {

        try {
            Event event = eventService.getEventById(eventId);
            guest.setEvent(event);

            if (guest.getGroupId() == null) {
                guestService.saveGuest(guest);
                redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Guest group added successfully"
                );
            } else {
                guestService.saveGuest(guest);
                redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Guest group updated successfully"
                );
            }

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                e.getMessage()
            );
        }

        return "redirect:/guest-management?eventId=" + eventId;
    }


    // =================================================
    // DELETE GUEST GROUP
    // =================================================
    @GetMapping("/guest-management/delete")
    public String deleteGuest(
            @RequestParam Long guestId,
            RedirectAttributes redirectAttributes) {

        Guest guest = guestService.getGuestById(guestId);
        Long eventId = guest.getEvent().getEventId();

        guestService.deleteGuest(guestId);

        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Guest group deleted successfully"
        );

        return "redirect:/guest-management?eventId=" + eventId;
    }
    
 // =================================================
    // EDIT GUEST GROUP
    // =================================================
    @GetMapping("/guest-management/edit")
    public String editGuest(
            @RequestParam Long guestId,
            Model model) {

        Guest guest = guestService.getGuestById(guestId);
        Event event = guest.getEvent();

        // Load dropdown data
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute(
            "events",
            eventService.getEventsByClient(
                event.getClient().getClientId()
            )
        );

        // Selected values
        model.addAttribute(
            "selectedClientId",
            event.getClient().getClientId()
        );
        model.addAttribute(
            "selectedEventId",
            event.getEventId()
        );

        // Populate form
        model.addAttribute("guest", guest);

        // Load table for same event
        model.addAttribute(
            "guests",
            guestService.getGuestsByEvent(event.getEventId())
        );

        return "guestManagement";
    }

}
