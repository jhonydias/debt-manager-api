package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.service.DebtCalculationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DebtCalculationServiceImpl implements DebtCalculationService {

    @Override
    public BigDecimal calculateDebtWithInterest(Debt debt) {
        BigDecimal principal = debt.getPrincipalAmount();
        BigDecimal rate = debt.getInterestRate().divide(BigDecimal.valueOf(100)); // Converter taxa para decimal
        int periods = debt.getNumberOfInstallments();

        // Calcular o valor da dívida com juros compostos
        return principal.multiply(BigDecimal.ONE.add(rate).pow(periods));
    }
}
