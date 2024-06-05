package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.model.entity.Payment;
import com.br.celcoin.debtmanagerapi.repository.PaymentRepository;
import com.br.celcoin.debtmanagerapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override public Page<Payment> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }
}
