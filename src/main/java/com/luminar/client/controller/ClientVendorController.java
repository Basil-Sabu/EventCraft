package com.luminar.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.luminar.entity.VendorAssignment;
import com.luminar.service.VendorAssignmentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientVendorController {
	
	@Autowired
	VendorAssignmentService vendorAssignmentService;
	
	@GetMapping("/client/vendors")
	public String myVendors(HttpSession session, Model model) {

	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null) {
	        return "redirect:/login";
	    }

	    List<VendorAssignment> assignments =
	            vendorAssignmentService.getVendorsByUserId(userId);

	    model.addAttribute("assignments", assignments);

	    return "client/vendorVED";
	}


}
