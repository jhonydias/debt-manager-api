package com.br.celcoin.debtmanagerapi.service;

import com.br.celcoin.debtmanagerapi.model.dto.request.DebtRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DebtService {
    @Transactional DebtResponseDto createDebt(DebtRequestDto dto);

    @Transactional Page<Debt> getAllDebts(Pageable pageable);

    @Transactional Debt getDebtById(Long id);
}
