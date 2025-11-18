package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.enums.PaymentStatus;
import com.payflow.paymentapi.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    PaymentService paymentService;

    @BeforeEach
    void setup() {
        PaymentRepository repo = new PaymentRepository();
        PaymentValidationService validation = new PaymentValidationService();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        PaymentProcessorService processor =
                new PaymentProcessorService(repo, validation, executor);

        paymentService = new PaymentService(repo, processor);
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
    void chargeShouldFailWhenAmountInvalid () throws Exception {
        PaymentRequest req = new PaymentRequest("U1", BigDecimal.ZERO, "IDR", "REFX", null);

        Payment p = paymentService.processSync(req);

        assertEquals(PaymentStatus.FAILED, p.getStatus());
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

        Payment payment = paymentService.processSync(req);

        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals("U1", payment.getUserId());
        assertEquals(BigDecimal.valueOf(15000), payment.getAmount());
    }
}
