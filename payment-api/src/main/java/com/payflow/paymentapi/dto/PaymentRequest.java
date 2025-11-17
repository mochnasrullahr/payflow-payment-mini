package com.payflow.paymentapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private String userId;
    private BigDecimal amount;
    private String currency;
    private String referenceId;
    private String orderId;


    // ➕ Tambahkan constructor sederhana khusus kebutuhan test
//    public PaymentRequest(String orderId, BigDecimal amount, String currency) {
//        this.orderId = orderId;
//        this.amount = amount;
//        this.currency = currency;
//    }

    public PaymentRequest(String referenceId, BigDecimal amount, String currency) {
        this.userId = "TEST";
        this.referenceId = referenceId;
        this.amount = amount;
        this.currency = currency;
    }

}
