package com.luminar.service;
import java.math.BigDecimal;
import java.util.List;

import com.luminar.entity.ClientPayment;

public interface ClientPaymentService {

    // ADD PAYMENT (insert only)
    void savePayment(ClientPayment payment);

    // PAYMENT HISTORY (EVENT-WISE)
    List<ClientPayment> getPaymentsByEvent(Long eventId);

    // TOTAL PAID SO FAR
    BigDecimal getTotalPaidAmountByEvent(Long eventId);

    // BALANCE DUE
    BigDecimal getBalanceDue(Long eventId);

    // GET SINGLE PAYMENT (if ever needed)
    ClientPayment getPaymentById(Long paymentId);
}

