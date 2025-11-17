package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.dto.ValidationResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentValidationService {

    public CompletableFuture<ValidationResult> validateAsync(PaymentRequest req) {
        return CompletableFuture.supplyAsync(() -> {

            if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                return new ValidationResult("INVALID", "Amount must be > 0");
            }

            if (req.getCurrency() == null || req.getCurrency().isBlank()) {
                return new ValidationResult("INVALID", "Currency invalid");
            }

            return new ValidationResult("VALID", "OK");
        });
    }
}
