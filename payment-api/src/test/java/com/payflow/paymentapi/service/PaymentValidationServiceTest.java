package com.payflow.paymentapi.service;

import com.payflow.paymentapi.dto.PaymentRequest;
import com.payflow.paymentapi.model.ValidationResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class PaymentValidationServiceTest {

    PaymentValidationService service = new PaymentValidationService();

    @Test
    void testValidRequest() throws Exception {
        var req = new PaymentRequest("ORD123", BigDecimal.valueOf(100), "USD");
        var result = service.validateAsync(req).get();
        assertEquals("VALID", result.status());
    }

    @Test
    void testInvalidAmount() throws Exception {
        var req = new PaymentRequest("ORD123", BigDecimal.ZERO, "USD");
        var result = service.validateAsync(req).get();
        assertEquals("INVALID", result.status());
    }
}
