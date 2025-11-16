package com.payflow.paymentapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.payflow.paymentapi.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
