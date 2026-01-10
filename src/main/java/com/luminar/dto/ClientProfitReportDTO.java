package com.luminar.dto;

import java.math.BigDecimal;

public class ClientProfitReportDTO {

    private String clientName;
    private BigDecimal totalPackage;
    private BigDecimal totalExpense;
    private BigDecimal profit;

    public ClientProfitReportDTO(
            String clientName,
            BigDecimal totalPackage,
            BigDecimal totalExpense) {

        this.clientName = clientName;
        this.totalPackage = totalPackage;
        this.totalExpense = totalExpense;
        this.profit = totalPackage.subtract(totalExpense);
    }

    public String getClientName() {
        return clientName;
    }

    public BigDecimal getTotalPackage() {
        return totalPackage;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getProfit() {
        return profit;
    }
}
