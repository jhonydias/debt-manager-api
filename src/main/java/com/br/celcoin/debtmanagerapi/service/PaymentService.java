package com.br.celcoin.debtmanagerapi.service;

import com.br.celcoin.debtmanagerapi.model.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Payment createPayment(Payment payment);

    Page<Payment> getAllPayments(Pageable pageable);
}
