package com.payflow.paymentapi.controller;

import com.payflow.paymentapi.dto.ApiResponse;
import com.payflow.paymentapi.dto.ChargeRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/charge")
    public ApiResponse<Payment> charge(@RequestBody PaymentRequest request) {
        log.info("Charge request received user={} amount={}", request.getUserId(), request.getAmount());

        try {
            Payment saved = paymentService.process(request);
            return ApiResponse.success("Charge processed", saved);
        } catch (IllegalArgumentException ex) {
            return ApiResponse.error("INVALID_REQUEST", ex.getMessage());
        } catch (Exception ex) {
            return ApiResponse.error("PROCESSING_ERROR", ex.getMessage());
        }
    }


    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
