package com.payflow.paymentapi.controller;

import com.payflow.paymentapi.dto.ApiResponse;
import com.payflow.paymentapi.dto.ChargeRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

//    @PostMapping("/charge")
//    public String charge() {
//        return "Charge processed";
//    }

    @PostMapping("/charge")
    public ApiResponse<String> charge(@RequestBody ChargeRequest request) {
        return ApiResponse.<String>builder()
                .status("SUCCESS")
                .message("Charge processed")
                .data("OK-" + request.getReferenceId())
                .build();
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
