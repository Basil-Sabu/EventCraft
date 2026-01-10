package com.luminar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.luminar.dto.ClientProfitReportDTO;
import com.luminar.service.ReportService;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    // =================================================
    // CLIENT PROFIT SUMMARY REPORT (ADMIN)
    // =================================================
    @GetMapping("/reports/client-profit")
    public String showClientProfitReport(Model model) {

        List<ClientProfitReportDTO> reportList =
                reportService.getClientProfitReport();

        model.addAttribute("reportList", reportList);

        return "clientProfitReport";
    }
}
