package com.br.celcoin.debtmanagerapi.service;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DebtService {
    Debt createDebt(Debt debt);

    Page<Debt> getAllDebts(Pageable pageable);

    Debt getDebtById(Long id);
}
