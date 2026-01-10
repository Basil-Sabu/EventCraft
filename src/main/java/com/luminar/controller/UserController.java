package com.luminar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.luminar.entity.User;
import com.luminar.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user,
                        HttpSession session,
                        Model model) {

        User loggedUser = userService.login(
                user.getUsername(), 
                user.getPassword()    
        );

        if (loggedUser != null) {

        	 // Store ONLY required details in session
            session.setAttribute("userId", loggedUser.getId());
            session.setAttribute("username", loggedUser.getUsername());
            session.setAttribute("role", loggedUser.getRole());

            if ("ADMIN".equals(loggedUser.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/client/dashboard";
            }
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
