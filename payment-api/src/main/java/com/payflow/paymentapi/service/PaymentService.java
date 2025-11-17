package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        var validationResult = validationService.validateAsync(req).get();

        if (!"VALID".equals(validationResult.status())) {
            throw new IllegalArgumentException(validationResult.message());
        }

        Payment payment = Payment.builder()
                .userId(req.getUserId())
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .referenceId(req.getReferenceId())
                .status("SUCCESS")
                .build();

        return paymentRepository.save(payment);
    }

    /**
     * Lightweight helper that skips validation (if you need it).
     */
    public Payment chargeWithoutValidation(Payment reqPayment) {
        return paymentRepository.save(reqPayment);
    }
}
