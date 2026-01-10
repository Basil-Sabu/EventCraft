package com.luminar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {

        // Check if user is logged in
        String role = (String) session.getAttribute("role");

        if (role == null) {
            // Not logged in
            return "redirect:/login";
        }

        if (!"ADMIN".equals(role)) {
            // Logged in but not admin
            return "redirect:/login";
        }

        return "adminDashboard";
    }
}
