package com.luminar.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.dto.ClientProfitReportDTO;
import com.luminar.repository.ReportRepository;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<ClientProfitReportDTO> getClientProfitReport() {

        List<Object[]> rows =
            reportRepository.fetchClientProfitData();

        List<ClientProfitReportDTO> reportList =
            new ArrayList<>();

        for (Object[] row : rows) {

            String clientName =
                (String) row[0];

            BigDecimal totalPackage =
                (BigDecimal) row[1];

            BigDecimal totalExpense =
                (BigDecimal) row[2];

            // Safety: handle nulls
            if (totalPackage == null)
                totalPackage = BigDecimal.ZERO;

            if (totalExpense == null)
                totalExpense = BigDecimal.ZERO;

            reportList.add(
                new ClientProfitReportDTO(
                    clientName,
                    totalPackage,
                    totalExpense
                )
            );
        }

        return reportList;
    }
}
