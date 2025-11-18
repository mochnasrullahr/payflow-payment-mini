package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.enums.PaymentStatus;
import com.payflow.paymentapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProcessorService processorService;

    /**
     * Create payment record with PENDING status, submit to background processor,
     * and return created Payment immediately.
     */
    public Payment submitPayment(PaymentRequest req) {
        // create entity with PENDING
        Payment payment = Payment.builder()
                .userId(req.getUserId())
                .amount(req.getAmount() == null ? BigDecimal.ZERO : req.getAmount())
                .currency(req.getCurrency())
                .referenceId(req.getReferenceId())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);
        log.info("Payment {} saved as PENDING, submitting to processor", payment.getReferenceId());

        // submit to async processor
        processorService.submit(payment, req);

        return payment;
    }

    // keep previous helper for synchronous processing if needed
    public Payment processSync(PaymentRequest req) throws Exception {
        Payment payment = Payment.builder()
                .userId(req.getUserId())
                .amount(req.getAmount())
                .currency(req.getCurrency())
                .referenceId(req.getReferenceId())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);

        processorService.process(payment, req);
        return payment;
    }
}
