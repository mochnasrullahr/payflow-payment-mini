package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.enums.PaymentStatus;
import com.payflow.paymentapi.repository.PaymentRepository;
import com.payflow.paymentapi.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceAsyncTest {

    PaymentRepository repo;
    PaymentValidationService validation;
    PaymentProcessorService processorService;
    PaymentService paymentService;

    @BeforeEach
    void setup() {
        repo = new com.payflow.paymentapi.repository.PaymentRepository(); // in-memory impl you already have
        validation = new PaymentValidationService();
        // use a small dedicated executor so tests complete fast
        ExecutorService exec = Executors.newSingleThreadExecutor();
        processorService = new PaymentProcessorService(repo, validation, exec);
        paymentService = new PaymentService(repo, processorService);
    }

    @Test
    void submitPaymentShouldBePendingThenSuccess() {
        String ref = "REF-" + UUID.randomUUID().toString().substring(0, 8);
        PaymentRequest req = new PaymentRequest("U1", BigDecimal.valueOf(12000), "IDR", ref, null);

        Payment created = paymentService.submitPayment(req);
        assertNotNull(created);
        assertEquals(PaymentStatus.PENDING, created.getStatus());

        // wait until processor updates status to SUCCESS (max 3s)
        await().atMost(Duration.ofSeconds(3))
                .until(() -> {
                    Payment p = repo.findByReferenceId(ref);
                    return p != null && p.getStatus() == PaymentStatus.SUCCESS;
                });
    }

    @Test
    void submitPaymentShouldBecomeFailedWhenInvalid() {
        String ref = "REF-" + UUID.randomUUID().toString().substring(0, 8);
        PaymentRequest req = new PaymentRequest("U1", BigDecimal.ZERO, "IDR", ref, null);

        Payment created = paymentService.submitPayment(req);
        assertEquals(PaymentStatus.PENDING, created.getStatus());

        await().atMost(Duration.ofSeconds(3))
                .until(() -> {
                    Payment p = repo.findByReferenceId(ref);
                    return p != null && p.getStatus() == PaymentStatus.FAILED;
                });
    }
}
