package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.enums.PaymentStatus;
import com.payflow.paymentapi.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    PaymentService paymentService;

    @BeforeEach
    void setup() {
        PaymentRepository repo = new PaymentRepository();
        PaymentValidationService validation = new PaymentValidationService();
        paymentService = new PaymentService(repo, validation);
    }

    @Test
    void paymentShouldStartAsPending() {
        Payment p = new Payment();
        p.setUserId("U1");
        p.setAmount(BigDecimal.valueOf(10000));
        p.setCurrency("IDR");
        p.setReferenceId("REF1");

        assertEquals(PaymentStatus.PENDING, p.getStatus());
    }

    @Test
    void chargeShouldFailWhenAmountInvalid() {
        PaymentRequest req = new PaymentRequest(
                "U1",
                BigDecimal.ZERO,
                "IDR",
                "REFX",
                null
        );

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.process(req);
        });
    }

    @Test
    void chargeShouldBeSuccessfulWhenValid() throws Exception {
        PaymentRequest req = new PaymentRequest(
                "U1",
                BigDecimal.valueOf(15000),
                "IDR",
                "REFY",
                null
        );

        Payment payment = paymentService.process(req);

        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals("U1", payment.getUserId());
        assertEquals(BigDecimal.valueOf(15000), payment.getAmount());
    }
}
