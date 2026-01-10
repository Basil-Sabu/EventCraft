package com.luminar.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luminar.entity.VendorExpense;

public interface VendorExpenseRepository
        extends JpaRepository<VendorExpense, Long> {

    // 🔹 1. Total paid amount for one assignment
    @Query("""
        SELECT COALESCE(SUM(ve.paidAmount), 0)
        FROM VendorExpense ve
        WHERE ve.assignment.assignmentId = :assignmentId
    """)
    BigDecimal getTotalPaidByAssignmentId(
        @Param("assignmentId") Long assignmentId
    );

    // 🔹 2. Payment history for one assignment
    List<VendorExpense> findByAssignmentAssignmentIdOrderByPaymentDateAsc(
        Long assignmentId
    );
}
