package com.br.celcoin.debtmanagerapi.model.dto.response;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.br.celcoin.debtmanagerapi.model.entity.Debt}
 */
public record DebtResponseDto(LocalDateTime dateCreate, Long id, BigDecimal totalAmount, BigDecimal principalAmount,
                              String creditorName, LocalDate dueDate, Integer numberOfInstallments,
                              BigDecimal interestRate,
                              DebtStatus status) implements Serializable {
    public static DebtResponseDto fromEntity(Debt save) {
        return new DebtResponseDto(
                save.getDateCreate(),
                save.getId(),
                save.getTotalAmount(),
                save.getPrincipalAmount(),
                save.getCreditorName(),
                save.getDueDate(),
                save.getNumberOfInstallments(),
                save.getInterestRate(),
                save.getStatus()
        );
    }
}