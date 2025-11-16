package com.payflow.paymentapi.dto;

import lombok.Data;

@Data
public class ChargeRequest {
    private String userId;
    private long amount;
    private String currency;
    private String referenceId;
}
