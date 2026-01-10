package com.luminar.service;

import java.math.BigDecimal;
import java.util.List;

import com.luminar.entity.VendorExpense;

public interface VendorExpenseService {

    // Save a new vendor payment
    void saveVendorExpense(VendorExpense expense);

    // Get total paid for an assignment
    BigDecimal getTotalPaid(Long assignmentId);

    // Get payment history
    List<VendorExpense> getPaymentHistory(Long assignmentId);
}
