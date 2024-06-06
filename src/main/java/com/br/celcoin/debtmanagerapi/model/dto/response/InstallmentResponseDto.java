package com.br.celcoin.debtmanagerapi.model.dto.response;

import com.br.celcoin.debtmanagerapi.model.entity.Installment;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentResponseDto(
        Long id,
        BigDecimal amount,
        LocalDate dueDate,
        boolean paid) {
    public static InstallmentResponseDto fromEntity(Installment installment) {
        return new InstallmentResponseDto(
                installment.getId(),
                installment.getAmount(),
                installment.getDueDate(),
                installment.isPaid()
        );
    }
}
