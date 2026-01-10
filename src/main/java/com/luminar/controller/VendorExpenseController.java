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

import com.luminar.entity.VendorAssignment;
import com.luminar.entity.VendorExpense;
import com.luminar.service.ClientService;
import com.luminar.service.EventService;
import com.luminar.service.VendorAssignmentService;
import com.luminar.service.VendorExpenseService;

@Controller
public class VendorExpenseController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EventService eventService;

    @Autowired
    private VendorAssignmentService assignmentService;

    @Autowired
    private VendorExpenseService vendorExpenseService;

    // =================================================
    // VENDOR EXPENSE ENTRY PAGE
    // =================================================
    @GetMapping("/vendor-expense")
    public String showVendorExpensePage(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long assignmentId,
            Model model) {

        // Load clients always
        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("vendorExpense", new VendorExpense());

        // If client selected → load events
        if (clientId != null) {
            model.addAttribute(
                "events",
                eventService.getEventsByClient(clientId)
            );
            model.addAttribute("selectedClientId", clientId);
        }

        // If event selected → load vendor assignments
        if (eventId != null) {
            model.addAttribute(
                "assignments",
                assignmentService.getAssignmentsByEvent(eventId)
            );
            model.addAttribute("selectedEventId", eventId);
        }

        // If vendor selected → load summary
        if (assignmentId != null) {

            VendorAssignment assignment =
                assignmentService.getAssignmentById(assignmentId);

            BigDecimal agreedCost =
                assignment.getAgreedCost();

            BigDecimal paidSoFar =
                vendorExpenseService.getTotalPaid(assignmentId);

            BigDecimal remaining =
                agreedCost.subtract(paidSoFar);

            model.addAttribute("assignment", assignment);
            model.addAttribute("agreedCost", agreedCost);
            model.addAttribute("paidSoFar", paidSoFar);
            model.addAttribute("remaining", remaining);
            model.addAttribute("selectedAssignmentId", assignmentId);
        }

        return "vendorExpense";
    }

    // =================================================
    // SAVE VENDOR PAYMENT
    // =================================================
    @PostMapping("/vendor-expense/save")
    public String saveVendorExpense(
            @ModelAttribute VendorExpense vendorExpense,
            @RequestParam Long clientId,
            @RequestParam Long eventId,
            @RequestParam Long assignmentId,
            RedirectAttributes redirectAttributes) {

        try {
            VendorAssignment assignment =
                assignmentService.getAssignmentById(assignmentId);

            vendorExpense.setAssignment(assignment);

            vendorExpenseService.saveVendorExpense(vendorExpense);

            redirectAttributes.addFlashAttribute(
                "successMessage",
                "Vendor payment recorded successfully"
            );

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                e.getMessage()
            );
        }

        return "redirect:/vendor-expense?clientId="
                + clientId
                + "&eventId=" + eventId
                + "&assignmentId=" + assignmentId;
    }

    // =================================================
    // VENDOR PAYMENT HISTORY PAGE
    // =================================================
    @GetMapping("/vendor-expense/history")
    public String showVendorPaymentHistory(
            @RequestParam Long assignmentId,
            Model model) {

        VendorAssignment assignment =
            assignmentService.getAssignmentById(assignmentId);

        model.addAttribute("assignment", assignment);

        model.addAttribute(
            "history",
            vendorExpenseService.getPaymentHistory(assignmentId)
        );

        model.addAttribute(
            "totalPaid",
            vendorExpenseService.getTotalPaid(assignmentId)
        );

        return "vendorExpenseHistory";
    }
}
