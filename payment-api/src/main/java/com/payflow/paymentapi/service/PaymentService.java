package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.enums.PaymentStatus;
import com.payflow.paymentapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentValidationService validationService;

    /**
     * Simple charge API used by controller.
     * This method performs validation synchronously via the async validator,
     * and throws IllegalArgumentException on invalid input.
     */
    public Payment process(PaymentRequest req) throws Exception {

        // 1. Create payment with PENDING
        Payment payment = Payment.builder()
                .userId(req.getUserId())
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .referenceId(req.getReferenceId())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);

        log.info("Payment {} created with PENDING", payment.getId());

        // 2. VALIDATING
        var validationResult = validationService.validateAsync(req).get();

        if (!validationResult.status().equals("VALID")) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            log.warn("Payment {} FAILED: {}", payment.getId(), validationResult.message());
            throw new IllegalArgumentException(validationResult.message());
        }

        // 3. SUCCESS
        payment.setStatus(PaymentStatus.SUCCESS);
        payment = paymentRepository.save(payment);

        log.info("Payment {} SUCCESS", payment.getId());

        return payment;
    }


    /**
     * Lightweight helper that skips validation (if you need it).
     */
    public Payment chargeWithoutValidation(Payment reqPayment) {
        return paymentRepository.save(reqPayment);
    }
}
