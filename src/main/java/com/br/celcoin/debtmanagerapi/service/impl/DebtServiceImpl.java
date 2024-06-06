package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.model.dto.request.DebtRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.request.DebtSeachRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtSeachResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import com.br.celcoin.debtmanagerapi.repository.specification.DebtSpecification;
import com.br.celcoin.debtmanagerapi.service.DebtService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtServiceImpl implements DebtService {
    private final DebtRepository debtRepository;

    @Autowired
    public DebtServiceImpl(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;
    }

    @Override public DebtResponseDto createDebt(DebtRequestDto dto) {
        final Debt debt = dto.toEntity();
        return DebtResponseDto.fromEntity(debtRepository.save(debt));
    }

    @Override
    public Page<DebtSeachResponseDto> searchDebts(DebtSeachRequestDto dto, Pageable pageable) {
        final Page<Debt> debts = debtRepository.findAll(DebtSpecification.search(dto), pageable);
        final List<DebtSeachResponseDto> response = debts.map(DebtSeachResponseDto::fromEntity).toList();
        return new PageImpl<>(response, pageable, debts.getTotalElements());
    }

    @Override
    public DebtSeachResponseDto getDebtById(Long id) {
        return DebtSeachResponseDto.fromEntity(findById(id));
    }

    public Debt findById(Long id) {
        return debtRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Debt not found. Id: " + id)
        );
    }
}
