package com.luminar.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.entity.VendorAssignment;
import com.luminar.entity.VendorExpense;
import com.luminar.repository.VendorAssignmentRepository;
import com.luminar.repository.VendorExpenseRepository;

@Service
public class VendorExpenseServiceImpl
        implements VendorExpenseService {

    @Autowired
    private VendorExpenseRepository vendorExpenseRepository;

    @Autowired
    private VendorAssignmentRepository vendorAssignmentRepository;

    // 🔹 SAVE PAYMENT (ADD ONLY)
    @Override
    public void saveVendorExpense(VendorExpense expense) {

        Long assignmentId =
            expense.getAssignment().getAssignmentId();

        VendorAssignment assignment =
            vendorAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Vendor assignment not found"
                    )
                );

        BigDecimal agreedCost = assignment.getAgreedCost();

        BigDecimal totalPaid =
            vendorExpenseRepository
                .getTotalPaidByAssignmentId(assignmentId);

        BigDecimal remaining =
            agreedCost.subtract(totalPaid);

        // ❌ No payment allowed if already fully paid
        if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                "Payment already completed for this vendor"
            );
        }

        // ❌ Paid amount validation
        if (expense.getPaidAmount() == null ||
            expense.getPaidAmount()
                   .compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                "Paid amount must be greater than zero"
            );
        }

        // ❌ Overpayment not allowed
        if (expense.getPaidAmount()
                   .compareTo(remaining) > 0) {

            throw new RuntimeException(
                "Paid amount exceeds remaining balance"
            );
        }

        // ❌ Final payment rule
        if ("FINAL".equalsIgnoreCase(
                expense.getPaymentType()
            ) &&
            expense.getPaidAmount()
                   .compareTo(remaining) != 0) {

            throw new RuntimeException(
                "Final payment must clear the remaining balance"
            );
        }

        // ✅ Auto-set payment date if not provided
        if (expense.getPaymentDate() == null) {
            expense.setPaymentDate(LocalDate.now());
        }

        // 🔒 Force correct assignment object
        expense.setAssignment(assignment);

        vendorExpenseRepository.save(expense);
    }

    // 🔹 TOTAL PAID
    @Override
    public BigDecimal getTotalPaid(Long assignmentId) {
        return vendorExpenseRepository
                .getTotalPaidByAssignmentId(assignmentId);
    }

    // 🔹 PAYMENT HISTORY
    @Override
    public List<VendorExpense> getPaymentHistory(
            Long assignmentId) {

        return vendorExpenseRepository
                .findByAssignmentAssignmentIdOrderByPaymentDateAsc(
                    assignmentId
                );
    }
}
