package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.model.dto.request.DebtRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.request.DebtSeachRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtSeachResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DebtServiceImplTest {

    @Mock
    private DebtRepository debtRepository;

    @InjectMocks
    private DebtServiceImpl debtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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

        Debt debt = debtRequestDto.toEntity();
        debt.setId(1L);
        debt.setPayments(new ArrayList<>());
        debt.setInstallments(new ArrayList<>());

        when(debtRepository.save(any(Debt.class))).thenReturn(debt);

        DebtResponseDto response = debtService.createDebt(debtRequestDto);

        assertNotNull(response);
        assertEquals(debt.getId(), response.id());
        assertEquals(debt.getPrincipalAmount(), response.principalAmount());
        assertEquals(debt.getCreditorName(), response.creditorName());
        assertEquals(debt.getDueDate(), response.dueDate());

        verify(debtRepository, times(1)).save(any(Debt.class));
    }

    @Test
    void testSearchDebts_Success() {
        DebtSeachRequestDto searchRequestDto = new DebtSeachRequestDto(
                "Creditor Name",
                DebtStatus.PENDING,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1),
                new BigDecimal("1000.00")
        );

        Debt debt = Debt.builder()
                .id(1L)
                .principalAmount(new BigDecimal("1000.00"))
                .creditorName("Creditor Name")
                .dueDate(LocalDate.now().plusMonths(6))
                .numberOfInstallments(6)
                .interestRate(new BigDecimal("0.05"))
                .status(DebtStatus.PENDING)
                .payments(new ArrayList<>())
                .installments(new ArrayList<>())
                .build();

        Page<Debt> page = new PageImpl<>(Collections.singletonList(debt));

        when(debtRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<DebtSeachResponseDto> response = debtService.searchDebts(searchRequestDto, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(debt.getId(), response.getContent().get(0).id());

        verify(debtRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testGetDebtById_Success() {
        Long debtId = 1L;
        Debt debt = Debt.builder()
                .id(debtId)
                .principalAmount(new BigDecimal("1000.00"))
                .creditorName("Creditor Name")
                .dueDate(LocalDate.now().plusMonths(6))
                .numberOfInstallments(6)
                .interestRate(new BigDecimal("0.05"))
                .status(DebtStatus.PENDING)
                .payments(new ArrayList<>())
                .installments(new ArrayList<>())
                .build();

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));

        DebtSeachResponseDto response = debtService.getDebtById(debtId);

        assertNotNull(response);
        assertEquals(debt.getId(), response.id());
        assertEquals(debt.getPrincipalAmount(), response.principalAmount());
        assertEquals(debt.getCreditorName(), response.creditorName());

        verify(debtRepository, times(1)).findById(debtId);
    }

    @Test
    void testGetDebtById_NotFound() {
        Long debtId = 1L;

        when(debtRepository.findById(debtId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> debtService.getDebtById(debtId));

        verify(debtRepository, times(1)).findById(debtId);
    }

    @Test
    void testFindById_Success() {
        Long debtId = 1L;
        Debt debt = Debt.builder()
                .id(debtId)
                .principalAmount(new BigDecimal("1000.00"))
                .creditorName("Creditor Name")
                .dueDate(LocalDate.now().plusMonths(6))
                .numberOfInstallments(6)
                .interestRate(new BigDecimal("0.05"))
                .status(DebtStatus.PENDING)
                .payments(new ArrayList<>())
                .installments(new ArrayList<>())
                .build();

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));

        Debt foundDebt = debtService.findById(debtId);

        assertNotNull(foundDebt);
        assertEquals(debt.getId(), foundDebt.getId());

        verify(debtRepository, times(1)).findById(debtId);
    }

    @Test
    void testFindById_NotFound() {
        Long debtId = 1L;

        when(debtRepository.findById(debtId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> debtService.findById(debtId));

        verify(debtRepository, times(1)).findById(debtId);
    }
}
