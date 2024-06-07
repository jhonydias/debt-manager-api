package com.br.celcoin.debtmanagerapi.service;

import com.br.celcoin.debtmanagerapi.model.dto.request.DebtRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.request.DebtSeachRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtSeachResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DebtService {
    DebtResponseDto createDebt(DebtRequestDto dto);

    Page<DebtSeachResponseDto> searchDebts(DebtSeachRequestDto dto, Pageable pageable);

    DebtSeachResponseDto getDebtById(Long id);
}
