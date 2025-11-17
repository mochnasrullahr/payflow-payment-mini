package com.payflow.paymentapi.service;

import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentRepository repo;
    private final PaymentValidationService validation;

    public Payment charge(PaymentRequest req) {
        Payment payment = Payment.builder()
                .userId(req.getUserId())
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .referenceId(req.getReferenceId())
                .status("SUCCESS")
                .build();

        return paymentRepository.save(payment);
    }

    public Payment process(PaymentRequest req) throws Exception {

        var validationResult = validation.validateAsync(req).get();

        if (!validationResult.status().equals("VALID")) {
            throw new IllegalArgumentException("Invalid payment: " + validationResult.message());
        }

        Payment payment = Payment.builder()
                .userId(req.getUserId())
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .referenceId(req.getReferenceId())
                .build();

        return repo.save(payment);
    }
}
