package com.payflow.paymentapi.controller;

import com.payflow.paymentapi.dto.ApiResponse;
import com.payflow.paymentapi.dto.ChargeRequest;
import com.payflow.paymentapi.entity.Payment;
import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;
//    @PostMapping("/charge")
//    public String charge() {
//        return "Charge processed";
//    }

    @PostMapping("/charge")
    public ApiResponse charge(@RequestBody ChargeRequest request) {
        return ApiResponse.<String>builder()
                .status("SUCCESS")
                .message("Charge processed")
                .data("OK-" + request.getReferenceId())
                .build();
    }

    @PostMapping("/charge")
    public ApiResponse charge(@RequestBody PaymentRequest req) {
        Payment saved = paymentService.charge(req);
        return ApiResponse.success("Charge processed", saved);
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
