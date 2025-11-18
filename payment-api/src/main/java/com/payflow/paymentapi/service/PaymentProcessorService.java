package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.enums.PaymentStatus;
import com.payflow.paymentapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentProcessorService {

    private final PaymentRepository paymentRepository;
    private final PaymentValidationService validationService;
    private final ExecutorService paymentExecutor; // bean name "paymentExecutor"

    /**
     * Submit payment to background executor for processing.
     * This method returns immediately; processing happens async.
     */
    public void submit(Payment payment, PaymentRequest request) {
        paymentExecutor.submit(() -> process(payment, request));
    }

    /**
     * The actual processing logic (can be called directly for sync tests).
     */
    public void process(Payment payment, PaymentRequest request) {
        try {
            log.info("Processing payment ref={} user={} amount={}", payment.getReferenceId(),
                    payment.getUserId(), payment.getAmount());

            var validationResult = validationService.validateAsync(request).get();

            if (!"VALID".equals(validationResult.status())) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                log.warn("Payment {} FAILED: {}", payment.getReferenceId(), validationResult.message());
                return;
            }

            // Simulated external payment gateway call could go here (with retries)
            // For demo, we mark success immediately
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);

            log.info("Payment {} SUCCESS", payment.getReferenceId());
        } catch (Exception ex) {
            log.error("Error processing payment {}: {}", payment.getReferenceId(), ex.getMessage(), ex);
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
        }
    }
}
