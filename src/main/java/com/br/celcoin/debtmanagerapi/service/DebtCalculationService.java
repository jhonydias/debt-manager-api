package com.br.celcoin.debtmanagerapi.service;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;

import java.math.BigDecimal;

public interface DebtCalculationService {
    BigDecimal calculateDebtWithInterest(Debt debt);
}
