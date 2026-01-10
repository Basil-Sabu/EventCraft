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
import com.luminar.entity.Vendor;
import com.luminar.entity.VendorAssignment;
import com.luminar.service.ClientService;
import com.luminar.service.EventService;
import com.luminar.service.VendorAssignmentService;
import com.luminar.service.VendorService;

@Controller
public class VendorAssignmentController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private EventService eventService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private VendorAssignmentService assignmentService;

    // ===============================
    // ADD / ASSIGN VENDOR PAGE
    // ===============================
    @GetMapping("/vendor-assignment/add")
    public String showAssignmentPage(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) String vendorType,
            @RequestParam(required = false) Long assignmentId,
            Model model) {

        // EDIT MODE
        if (assignmentId != null) {
            VendorAssignment assignment =
                    assignmentService.getAssignmentById(assignmentId);

            model.addAttribute("assignment", assignment);

            Event event = assignment.getEvent();
            model.addAttribute("event", event);
            model.addAttribute("selectedEventId", event.getEventId());

            Long selectedClientId = event.getClient().getClientId();
            model.addAttribute("selectedClientId", selectedClientId);

            model.addAttribute(
                "events",
                eventService.getEventsByClient(selectedClientId)
            );

            model.addAttribute("selectedVendorType",
                    assignment.getVendor().getVendorType());

            model.addAttribute(
                "vendors",
                vendorService.getVendorsByType(
                    assignment.getVendor().getVendorType()
                )
            );

        } else {
            // ADD MODE
            model.addAttribute("assignment", new VendorAssignment());

            if (clientId != null) {
                model.addAttribute("selectedClientId", clientId);
                model.addAttribute(
                    "events",
                    eventService.getEventsByClient(clientId)
                );
            }

            if (eventId != null) {
                model.addAttribute("selectedEventId", eventId);
                model.addAttribute("event",
                    eventService.getEventById(eventId));
            }

            if (vendorType != null) {
                model.addAttribute("selectedVendorType", vendorType);
                model.addAttribute(
                    "vendors",
                    vendorService.getVendorsByType(vendorType)
                );
            }
        }

        model.addAttribute("clients", clientService.getAllClients());
        model.addAttribute("vendorTypes", vendorService.getVendorTypes());

        return "vendorAssignmentAdd";
    }

    // ===============================
    // SAVE ASSIGNMENT
    // ===============================
    @PostMapping("/vendor-assignment/save")
    public String saveAssignment(
            @ModelAttribute VendorAssignment assignment,
            @RequestParam Long eventId,
            @RequestParam Long vendorId,
            RedirectAttributes redirectAttributes) {

        Event event = eventService.getEventById(eventId);
        Vendor vendor = vendorService.getVendorById(vendorId);

        assignment.setEvent(event);
        assignment.setVendor(vendor);

        assignmentService.assignVendor(assignment); // save()

        redirectAttributes.addFlashAttribute(
            "successMessage",
            assignment.getAssignmentId() == null
                ? "Vendor assigned successfully"
                : "Vendor assignment updated successfully"
        );

        return "redirect:/vendor-assignment/add?eventId=" + eventId;
    }


    // ===============================
    // VIEW ASSIGNED VENDORS (READ ONLY)
    // ===============================
    @GetMapping("/vendor-assignment/list")
    public String viewAssignments(
            @RequestParam Long eventId,
            Model model) {

        Event event = eventService.getEventById(eventId);

        model.addAttribute("event", event);
        model.addAttribute(
            "assignments",
            assignmentService.getAssignmentsByEvent(eventId)
        );

        return "vendorAssignmentVED";
    }
 // ===============================
 // DELETE ASSIGNMENT
 // ===============================
 @GetMapping("/vendor-assignment/delete")
 public String deleteAssignment(
         @RequestParam Long assignmentId,
         @RequestParam Long eventId,
         RedirectAttributes redirectAttributes) {

     assignmentService.deleteAssignment(assignmentId);

     redirectAttributes.addFlashAttribute(
         "successMessage",
         "Vendor assignment deleted successfully"
     );

     return "redirect:/vendor-assignment/list?eventId=" + eventId;
 }

}
