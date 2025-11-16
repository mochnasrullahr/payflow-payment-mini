package com.payflow.paymentapi.repository;

import org.springframework.stereotype.Repository;
import com.payflow.paymentapi.entity.Payment;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Repository
public class PaymentRepository {

    private final Map<String, Payment> storage = new ConcurrentHashMap<>();

    public Payment save(Payment payment) {
        storage.put(payment.getReferenceId(), payment);
        return payment;
    }

    public Payment findByReferenceId(String refId) {
        return storage.get(refId);
    }
}
