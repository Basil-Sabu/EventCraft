package com.luminar.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.luminar.entity.ClientPayment;
import com.luminar.entity.VendorAssignment;

public interface ClientPaymentRepository extends JpaRepository<ClientPayment, Long> {

    // ===============================
    // PAYMENT HISTORY (EVENT-WISE)
    // ===============================
    List<ClientPayment> findByEventEventIdOrderByPaymentDateAsc(Long eventId);

    // ===============================
    // TOTAL PAID AMOUNT (EVENT-WISE)
    // ===============================
    @Query("""
           SELECT COALESCE(SUM(cp.paidAmount), 0)
           FROM ClientPayment cp
           WHERE cp.event.eventId = :eventId
           """)
    BigDecimal getTotalPaidAmountByEvent(Long eventId);

    // ===============================
    // PAYMENT HISTORY (CLIENT + EVENT)
    // ===============================
    List<ClientPayment> findByClientClientIdAndEventEventIdOrderByPaymentDateAsc(
            Long clientId,
            Long eventId
    );
    
    List<ClientPayment> findByClientUserId(Long userId);
}
