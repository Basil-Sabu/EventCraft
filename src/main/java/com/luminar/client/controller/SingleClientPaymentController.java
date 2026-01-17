package com.luminar.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.luminar.entity.ClientPayment;
import com.luminar.service.ClientPaymentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class SingleClientPaymentController {
	
	@Autowired
	ClientPaymentService paymentService;
	
	@GetMapping("/client/payments")
	public String myPayments(HttpSession session, Model model) {

	    Long userId = (Long) session.getAttribute("userId");
	    if (userId == null) {
	        return "redirect:/login";
	    }

	    List<ClientPayment> payments =
	    		paymentService.getPaymentsByUserId(userId);

	    model.addAttribute("payments", payments);

	    return "client/payment";
	}

}
