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
        log.info("Start processing payment user={} amount={} ref={}",
                req.getUserId(), req.getAmount(), req.getReferenceId());

        var validationResult = validationService.validateAsync(req).get();

        if (!validationResult.status().equals("VALID")) {
            log.warn("Payment invalid: {}", validationResult.message());
            throw new IllegalArgumentException(validationResult.message());
        }

        Payment payment = Payment.builder()
                .userId(req.getUserId())
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .referenceId(req.getReferenceId())
                .status(PaymentStatus.SUCCESS)
                .build();

        Payment saved = paymentRepository.save(payment);

        log.info("Payment success id={}", saved.getId());
        return saved;
    }

    /**
     * Lightweight helper that skips validation (if you need it).
     */
    public Payment chargeWithoutValidation(Payment reqPayment) {
        return paymentRepository.save(reqPayment);
    }
}
