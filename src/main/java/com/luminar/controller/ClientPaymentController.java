package com.luminar.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luminar.entity.Client;
import com.luminar.entity.ClientPayment;
import com.luminar.entity.Event;
import com.luminar.service.ClientPaymentService;
import com.luminar.service.ClientService;
import com.luminar.service.EventService;

@Controller
public class ClientPaymentController {

    @Autowired
    private ClientPaymentService paymentService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EventService eventService;

    // =================================================
    // PAYMENT ENTRY PAGE
    // =================================================
    @GetMapping("/client-payments")
    public String showPaymentPage(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long eventId,
            Model model) {

        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("payment", new ClientPayment());

        // If client selected → load events
        if (clientId != null) {
            model.addAttribute(
                "events",
                eventService.getEventsByClient(clientId)
            );
            model.addAttribute("selectedClientId", clientId);
        }

        // If event selected → load summary
        if (eventId != null) {
            Event event = eventService.getEventById(eventId);

            BigDecimal paidSoFar =
                    paymentService.getTotalPaidAmountByEvent(eventId);

            BigDecimal balance =
                    paymentService.getBalanceDue(eventId);

            model.addAttribute("event", event);
            model.addAttribute("paidSoFar", paidSoFar);
            model.addAttribute("balanceDue", balance);
            model.addAttribute("selectedEventId", eventId);
        }

        return "clientPayment";
    }

    // =================================================
    // SAVE PAYMENT
    // =================================================
    @PostMapping("/client-payments/save")
    public String savePayment(
            @ModelAttribute ClientPayment payment,
            @RequestParam Long clientId,
            @RequestParam Long eventId,
            RedirectAttributes redirectAttributes) {

        try {
            Client client = clientService.getClientById(clientId);
            Event event = eventService.getEventById(eventId);

            payment.setClient(client);
            payment.setEvent(event);

            paymentService.savePayment(payment);

            redirectAttributes.addFlashAttribute(
                "successMessage",
                "Payment recorded successfully"
            );

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                e.getMessage()
            );
        }

        return "redirect:/client-payments?clientId="
                + clientId + "&eventId=" + eventId;
    }

    // =================================================
    // PAYMENT HISTORY PAGE
    // =================================================
    @GetMapping("/client-payments/history")
    public String showPaymentHistory(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long eventId,
            Model model) {

        model.addAttribute("clients", clientService.getAllClients());

        // Load events if client selected
        if (clientId != null) {
            model.addAttribute(
                "events",
                eventService.getEventsByClient(clientId)
            );
            model.addAttribute("selectedClientId", clientId);
        }

        // Load payment history if event selected
        if (eventId != null) {
            model.addAttribute(
                "payments",
                paymentService.getPaymentsByEvent(eventId)
            );

            Event event = eventService.getEventById(eventId);

            BigDecimal paidSoFar =
                    paymentService.getTotalPaidAmountByEvent(eventId);

            BigDecimal balance =
                    paymentService.getBalanceDue(eventId);

            model.addAttribute("event", event);
            model.addAttribute("paidSoFar", paidSoFar);
            model.addAttribute("balanceDue", balance);
            model.addAttribute("selectedEventId", eventId);
        }

        return "clientPaymentHistory";
    }
}
