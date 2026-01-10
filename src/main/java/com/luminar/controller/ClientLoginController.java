package com.luminar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientLoginController {

    @GetMapping("/client/dashboard")
    public String clientDashboard(HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null || !"CLIENT".equals(role)) {
            return "redirect:/";
        }

        return "client/clientDashboard";
    }
}
