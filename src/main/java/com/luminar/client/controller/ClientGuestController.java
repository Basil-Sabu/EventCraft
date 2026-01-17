
package com.luminar.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luminar.entity.Guest;
import com.luminar.service.EventService;
import com.luminar.service.GuestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientGuestController {

    @Autowired
    private GuestService guestService;
    @Autowired
    private EventService eventService;

    @GetMapping("/client/guests")
    public String guestSummaryPage(
            @RequestParam(required = false) Long eventId,
            HttpSession session,
            Model model) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/";
        }

        // Load client's events for dropdown
        model.addAttribute(
            "events",
            eventService.getEventsByUserId(userId)
        );

        if (eventId != null) {
            List<Guest> guests =
                guestService.getGuestGroupsForClientEvent(eventId, userId);

            int total = guests.stream()
                    .mapToInt(Guest::getGuestCount)
                    .sum();

            model.addAttribute("guests", guests);
            model.addAttribute("totalGuestCount", total);
            model.addAttribute("selectedEventId", eventId);
        }

        return "client/guestVED";
    }
}

