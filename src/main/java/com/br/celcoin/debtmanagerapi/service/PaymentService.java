package com.br.celcoin.debtmanagerapi.service;

import com.br.celcoin.debtmanagerapi.model.dto.request.PaymentRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.InstallmentResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto registerPayment(PaymentRequestDto dto);

    List<InstallmentResponseDto> getInstallments(Long debtId);

    InstallmentResponseDto getNextInstallmentToPay(Long debtId);
}
