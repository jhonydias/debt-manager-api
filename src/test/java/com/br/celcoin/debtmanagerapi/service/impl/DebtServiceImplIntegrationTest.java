package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.model.dto.request.DebtRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.request.DebtSeachRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtSeachResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DebtServiceImplIntegrationTest {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private DebtServiceImpl debtService;

    private Debt debt;

    @BeforeEach
    public void setUp() {
        debt = Debt.builder()
                .principalAmount(new BigDecimal("1000.00"))
                .interestRate(new BigDecimal("0.05"))
                .numberOfInstallments(2)
                .creditorName("Creditor")
                .dueDate(LocalDate.now().plusMonths(1))
                .build();
        debt.prePersist();
        debt = debtRepository.save(debt);
    }

    @Test
    void testCreateDebt_Success() {
        DebtRequestDto debtRequestDto = new DebtRequestDto(
                new BigDecimal("1000.00"),
                new BigDecimal("0.05"),
                "Creditor Name",
                LocalDate.now().plusMonths(6),
                6
        );

        DebtResponseDto response = debtService.createDebt(debtRequestDto);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(debtRequestDto.principalAmount(), response.principalAmount());
        assertEquals(debtRequestDto.creditorName(), response.creditorName());
        assertEquals(debtRequestDto.dueDate(), response.dueDate());

        Debt createdDebt = debtRepository.findById(response.id()).orElse(null);
        assertNotNull(createdDebt);
        assertEquals(createdDebt.calculateCompoundInterest(), createdDebt.getTotalAmount());
    }

    @Test
    void testSearchDebts_Success() {
        DebtSeachRequestDto searchRequestDto = new DebtSeachRequestDto(
                "Creditor",
                DebtStatus.PENDING,
                null,
                null,
                null);

        Pageable pageable = PageRequest.of(0, 10);
        Page<DebtSeachResponseDto> response = debtService.searchDebts(searchRequestDto, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(debt.getId(), response.getContent().get(0).id());
    }

    @Test
    void testGetDebtById_Success() {
        DebtSeachResponseDto response = debtService.getDebtById(debt.getId());

        assertNotNull(response);
        assertEquals(debt.getId(), response.id());
        assertEquals(debt.getPrincipalAmount(), response.principalAmount());
        assertEquals(debt.getCreditorName(), response.creditorName());
    }
}
