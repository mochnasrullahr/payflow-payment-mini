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
}
