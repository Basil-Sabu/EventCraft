package com.luminar.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "vendor_expenses")
public class VendorExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    // Many payments → One vendor assignment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private VendorAssignment assignment;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal paidAmount;

    // ADVANCE / FINAL
    @Column(nullable = false, length = 20)
    private String paymentType;

    // CASH / UPI / BANK
    @Column(nullable = false, length = 20)
    private String paymentMode;

    @Column(nullable = false)
    private LocalDate paymentDate;

    public VendorExpense() {}

    public Long getExpenseId() {
        return expenseId;
    }

    public VendorAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(VendorAssignment assignment) {
        this.assignment = assignment;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
