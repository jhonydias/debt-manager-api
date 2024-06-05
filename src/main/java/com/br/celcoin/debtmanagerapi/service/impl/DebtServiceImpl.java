package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import com.br.celcoin.debtmanagerapi.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DebtServiceImpl implements DebtService {
    private final DebtRepository debtRepository;

    @Autowired
    public DebtServiceImpl(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;
    }

    @Override public Debt createDebt(Debt debt) {
        return debtRepository.save(debt);
    }

    @Override public Page<Debt> getAllDebts(Pageable pageable) {
        return debtRepository.findAll(pageable);
    }

    @Override public Debt getDebtById(Long id) {
        return debtRepository.findById(id).orElse(null);
    }
}
