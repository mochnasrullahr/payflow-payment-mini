package com.payflow.paymentapi.model;

import java.util.List;

public record ValidationResult(
        String status,
        List<String> errors
) {
    public static ValidationResult valid() {
        return new ValidationResult("VALID", List.of());
    }

    public static ValidationResult invalid(String error) {
        return new ValidationResult("INVALID", List.of(error));
    }
}
