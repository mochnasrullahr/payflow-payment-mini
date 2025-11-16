package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.model.ValidationResult;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PaymentValidationService {

    @Retry(name = "validatorRetry")
    @TimeLimiter(name = "validatorTimeout")
    public CompletableFuture<ValidationResult> validateAsync(PaymentRequest req) {

        return CompletableFuture.supplyAsync(() -> {
            log.info("Validating payment: {}", req.getOrderId());

            if (req.getAmount() == null || req.getAmount().doubleValue() <= 0) {
                return ValidationResult.invalid("Amount must be > 0");
            }

            if (req.getCurrency() == null || req.getCurrency().isBlank()) {
                return ValidationResult.invalid("Currency is empty");
            }

            return ValidationResult.valid();
        });
    }
}
