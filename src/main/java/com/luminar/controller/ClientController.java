package com.luminar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luminar.entity.Client;
import com.luminar.entity.User;
import com.luminar.service.ClientService;
import com.luminar.service.UserService;

@Controller
public class ClientController {
	
	@Autowired
	ClientService clientService;
	
	@Autowired
	UserService userService;
	
	@GetMapping("/client")
	public String showClientPage(Model m) {
		m.addAttribute("client",new Client());
		return "clientDetails";
		
	}
	
	@PostMapping("/saveClient")
	public String saveClient(
	        @ModelAttribute("client") Client client,
	        @RequestParam("username") String username,
	        @RequestParam(value = "password", required = false) String password,
	        RedirectAttributes redirectAttributes) {

	    try {

	        if (client.getClientId() == null) {
	            // ADD

	            if (password == null || password.isBlank()) {
	                redirectAttributes.addFlashAttribute(
	                    "errorMessage",
	                    "Password is required for new client"
	                );
	                return "redirect:/client";
	            }

	          
	            clientService.saveClientWithLogin(client, username, password);
	            redirectAttributes.addFlashAttribute("successMessage", "Client created successfully");

	        } else {
	            // UPDATE
	            clientService.updateClientWithLogin(client, username, password);
	            redirectAttributes.addFlashAttribute(
	                "successMessage",
	                "Client updated successfully"
	            );
	        }

	    } catch (RuntimeException e) {
	        redirectAttributes.addFlashAttribute(
	            "errorMessage",
	            e.getMessage()
	        );
	    }

	    return "redirect:/client";
	}


		
	
	
	@GetMapping("/clientTable")	
    public String viewClientTage(Model model) {
        model.addAttribute("listClients", clientService.getAllClients());
        return "ClientVED";
    }
	
	@GetMapping("/client/edit/{id}")
	public String editClient(@PathVariable Long id, Model model) {

	    Client client = clientService.getClientById(id);
	    User user = userService.findById(client.getUserId());

	    // OR however you map client ↔ user

	    model.addAttribute("client", client);
	    model.addAttribute("username", user.getUsername());

	    return "clientDetails";
	}

	
	@GetMapping("/client/delete/{id}")
	public String deleteClient(
	        @PathVariable Long id,
	        RedirectAttributes redirectAttributes) {

	    clientService.deleteClient(id);

	    redirectAttributes.addFlashAttribute(
	        "successMessage",
	        "Client deleted successfully"
	    );

	    return "redirect:/clientTable";
	}
	

	

}
