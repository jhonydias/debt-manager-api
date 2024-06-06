package com.br.celcoin.debtmanagerapi.model.dto.response;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * DTO for {@link com.br.celcoin.debtmanagerapi.model.entity.Debt}
 */
public record DebtSeachResponseDto(Long id, BigDecimal totalAmount, BigDecimal principalAmount, String creditorName,
                                   LocalDate dueDate, Integer numberOfInstallments, BigDecimal interestRate, DebtStatus status,
                                   LocalDate lastPaymentDate, List<PaymentResponseDto> payments,
                                   List<InstallmentResponseDto> installments, BigDecimal remainingAmount) implements Serializable {

    public static DebtSeachResponseDto fromEntity(Debt save) {
        return new DebtSeachResponseDto(
                save.getId(),
                save.getTotalAmount(),
                save.getPrincipalAmount(),
                save.getCreditorName(),
                save.getDueDate(),
                save.getNumberOfInstallments(),
                save.getInterestRate(),
                save.getStatus(),
                save.getLastPaymentDate(),
                save.getPayments() != null ? save.getPayments().stream().map(PaymentResponseDto::fromEntity).toList() : Collections.emptyList(),
                save.getInstallments() != null ? save.getInstallments().stream().map(InstallmentResponseDto::fromEntity).toList() : Collections.emptyList(),
                save.getRemainingAmount()
        );
    }
}