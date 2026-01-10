package com.luminar.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.entity.ClientPayment;
import com.luminar.entity.Event;
import com.luminar.repository.ClientPaymentRepository;
import com.luminar.repository.EventRepository;

@Service
public class ClientPaymentServiceImpl implements ClientPaymentService {

    @Autowired
    private ClientPaymentRepository paymentRepository;

    @Autowired
    private EventRepository eventRepository;

    // ===============================
    // SAVE PAYMENT (INSERT ONLY)
    // ===============================
    @Override
    public void savePayment(ClientPayment payment) {

        Long eventId = payment.getEvent().getEventId();

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                    new RuntimeException("Event not found")
                );

        BigDecimal packageAmount = event.getPackageAmount();

        BigDecimal paidSoFar =
                paymentRepository.getTotalPaidAmountByEvent(eventId);

        BigDecimal newTotal =
                paidSoFar.add(payment.getPaidAmount());

        //  Overpayment check
        if (newTotal.compareTo(packageAmount) > 0) {
            throw new RuntimeException(
                "Payment exceeds the total package amount"
            );
        }

        // Auto-set payment date if not provided
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDate.now());
        }

        paymentRepository.save(payment);
    }

    // ===============================
    // PAYMENT HISTORY (EVENT-WISE)
    // ===============================
    @Override
    public List<ClientPayment> getPaymentsByEvent(Long eventId) {
        return paymentRepository
                .findByEventEventIdOrderByPaymentDateAsc(eventId);
    }

    // ===============================
    // TOTAL PAID SO FAR
    // ===============================
    @Override
    public BigDecimal getTotalPaidAmountByEvent(Long eventId) {
        return paymentRepository.getTotalPaidAmountByEvent(eventId);
    }

    // ===============================
    // BALANCE DUE
    // ===============================
    @Override
    public BigDecimal getBalanceDue(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                    new RuntimeException("Event not found")
                );

        BigDecimal packageAmount = event.getPackageAmount();
        BigDecimal paidSoFar = getTotalPaidAmountByEvent(eventId);

        return packageAmount.subtract(paidSoFar);
    }

    // ===============================
    // GET SINGLE PAYMENT
    // ===============================
    @Override
    public ClientPayment getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                    new RuntimeException("Payment not found")
                );
    }
}
