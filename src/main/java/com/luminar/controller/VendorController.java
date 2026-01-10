package com.luminar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luminar.entity.Vendor;
import com.luminar.service.VendorService;

@Controller
public class VendorController {

    @Autowired
    private VendorService vendorService;

    // ===============================
    // SHOW ADD VENDOR PAGE
    // ===============================
    @GetMapping("/vendors/add")
    public String showAddVendorPage(Model model) {
        model.addAttribute("vendor", new Vendor());
        return "vendorForm";
    }

    // ===============================
    // SAVE / UPDATE VENDOR
    // ===============================
    @PostMapping("/vendors/save")
    public String saveVendor(
            @ModelAttribute Vendor vendor,
            RedirectAttributes redirectAttributes) {

        try {
            if (vendor.getVendorId() == null) {
                vendorService.saveVendor(vendor);
                redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Vendor added successfully"
                );
            } else {
                vendorService.saveVendor(vendor);
                redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Vendor updated successfully"
                );
            }

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                e.getMessage()
            );
        }

        return "redirect:/vendors/add";
    }

    // ===============================
    // VENDOR LIST PAGE
    // ===============================
    @GetMapping("/vendors/list")
    public String listVendors(Model model) {
        model.addAttribute(
            "vendors",
            vendorService.getAllVendors()
        );
        return "vendorVED";
    }

    // ===============================
    // EDIT VENDOR
    // ===============================
    @GetMapping("/vendors/edit/{id}")
    public String editVendor(
            @PathVariable Long id,
            Model model) {

        Vendor vendor = vendorService.getVendorById(id);
        model.addAttribute("vendor", vendor);

        return "vendorForm";
    }

    // ===============================
    // DELETE VENDOR
    // ===============================
    @GetMapping("/vendors/delete/{id}")
    public String deleteVendor(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        vendorService.deleteVendor(id);

        redirectAttributes.addFlashAttribute(
            "successMessage",
            "Vendor deleted successfully"
        );

        return "redirect:/vendors/list";
    }
}
