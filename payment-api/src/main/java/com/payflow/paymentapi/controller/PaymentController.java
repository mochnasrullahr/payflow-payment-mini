package com.payflow.paymentapi.controller;

import com.payflow.paymentapi.model.PaymentRequest;
import com.payflow.paymentapi.model.ValidationResult;
import com.payflow.paymentapi.service.PaymentValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentValidationService validationService;

    @PostMapping("/validate")
    public CompletableFuture<ResponseEntity<ValidationResult>> validate(
            @RequestBody PaymentRequest request
    ) {
        return validationService.validateAsync(request)
                .thenApply(ResponseEntity::ok);
    }
}
