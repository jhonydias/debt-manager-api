package com.br.celcoin.debtmanagerapi;

import com.br.celcoin.debtmanagerapi.model.dto.request.DebtRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import com.br.celcoin.debtmanagerapi.service.DebtService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DebtServiceTest {

    @InjectMocks
    private DebtService debtService;

    @Mock
    private DebtRepository debtRepository;

    public DebtServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDebt() {
        DebtRequestDto dto = new DebtRequestDto(
                new BigDecimal("1000"), 
                new BigDecimal("0.05"), 
                "Creditor Name",
                LocalDate.now().plusMonths(6), 
                6
        );

        Debt mockDebt = dto.toEntity();
        mockDebt.setId(1L);
        mockDebt.setTotalAmount(new BigDecimal("1300")); // assumindo que o cálculo de juros está correto
        when(debtRepository.save(any(Debt.class))).thenReturn(mockDebt);

        DebtResponseDto response = debtService.createDebt(dto);

        assertEquals(new BigDecimal("1300"), response.totalAmount());
        assertEquals(new BigDecimal("1000"), response.principalAmount());
        assertEquals("Creditor Name", response.creditorName());
        assertEquals(6, response.numberOfInstallments());
        assertEquals(new BigDecimal("0.05"), response.interestRate());
    }
}
