package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.model.dto.request.PaymentRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.InstallmentResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.PaymentResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.entity.Installment;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import com.br.celcoin.debtmanagerapi.repository.InstallmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PaymentServiceImplIntegrationTest {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private InstallmentRepository installmentRepository;

    @Autowired
    private PaymentServiceImpl paymentService;

    private Debt debt;
    private Installment installment;

    @BeforeEach
    public void setUp() {
        debt = Debt.builder()
                .principalAmount(new BigDecimal("1000.00"))
                .interestRate(new BigDecimal("0.05"))
                .numberOfInstallments(2)
                .creditorName("Creditor")
                .dueDate(LocalDate.now().plusMonths(1))
                .build();
        debt = debtRepository.save(debt);

        installment = debt.getInstallments().get(0);
    }

    @Test
    void testRegisterPayment_Success() {
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(debt.getId());

        PaymentResponseDto response = paymentService.registerPayment(paymentRequestDto);

        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(installment.getAmount(), response.amount());

        Optional<Installment> updatedInstallment = installmentRepository.findById(installment.getId());
        assertTrue(updatedInstallment.isPresent());
        assertTrue(updatedInstallment.get().isPaid());

        Optional<Debt> updatedDebt = debtRepository.findById(debt.getId());
        assertTrue(updatedDebt.isPresent());
        assertNotNull(updatedDebt.get().getLastPaymentDate());
    }

    @Test
    void testGetNextInstallmentToPay_Success() {
        InstallmentResponseDto response = paymentService.getNextInstallmentToPay(debt.getId());

        assertNotNull(response);
        assertEquals(installment.getId(), response.id());
        assertEquals(installment.getAmount(), response.amount());
        assertEquals(installment.getDueDate(), response.dueDate());
    }
}
