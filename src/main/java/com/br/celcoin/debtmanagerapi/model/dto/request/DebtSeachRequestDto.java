package com.br.celcoin.debtmanagerapi.model.dto.request;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link Debt}
 */
public record DebtSeachRequestDto(String creditorName, DebtStatus status, LocalDate startDate, LocalDate endDate,
                                  BigDecimal principalAmount) implements Serializable {
}