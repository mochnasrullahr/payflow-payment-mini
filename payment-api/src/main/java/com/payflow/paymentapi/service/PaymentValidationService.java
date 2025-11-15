package com.payflow.paymentapi.service;

import com.payflow.paymentapi.model.PaymentRequest;
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
            log.info("Validating payment: {}", req.orderId());

            if (req.amount() == null || req.amount().doubleValue() <= 0) {
                return ValidationResult.invalid("Amount must be > 0");
            }

            if (req.currency() == null || req.currency().isBlank()) {
                return ValidationResult.invalid("Currency is empty");
            }

            return ValidationResult.valid();
        });
    }
}
