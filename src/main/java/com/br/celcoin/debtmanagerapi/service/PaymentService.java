package com.br.celcoin.debtmanagerapi.service;

import com.br.celcoin.debtmanagerapi.model.dto.request.PaymentRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.InstallmentResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.PaymentResponseDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface PaymentService {
    @Transactional
    PaymentResponseDto registerPayment(PaymentRequestDto dto);

    @Transactional
    List<InstallmentResponseDto> getInstallments(Long debtId);

    @Transactional
    InstallmentResponseDto getNextInstallmentToPay(Long debtId);
}
