package com.br.celcoin.debtmanagerapi.model.entity;

import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DebtTest {

    private Debt debt;
    private BigDecimal expectedInstallmentAmount;

    @BeforeEach
    public void setUp() {
        debt = Debt.builder()
                .principalAmount(new BigDecimal("1000.00"))
                .creditorName("Creditor")
                .dueDate(LocalDate.now().plusMonths(1))
                .numberOfInstallments(2)
                .interestRate(new BigDecimal("0.05"))
                .payments(new ArrayList<>())
                .installments(new ArrayList<>())
                .build();
        debt.prePersist();
        BigDecimal onePlusRate = BigDecimal.ONE.add(debt.getInterestRate());
        BigDecimal compoundFactor = onePlusRate.pow(debt.getNumberOfInstallments());
        BigDecimal totalAmount = debt.getPrincipalAmount().multiply(compoundFactor).setScale(2, RoundingMode.HALF_UP);
        expectedInstallmentAmount = totalAmount.divide(BigDecimal.valueOf(debt.getNumberOfInstallments()), 2, RoundingMode.HALF_UP);
    }

    @Test
    void testGenerateInstallments() {
        List<Installment> installments = debt.getInstallments();

        assertEquals(2, installments.size());
        assertEquals(expectedInstallmentAmount, installments.get(0).getAmount());
        assertEquals(LocalDate.now().plusMonths(1), installments.get(0).getDueDate());
        assertFalse(installments.get(0).isPaid());
    }

    @Test
    void testCalculateCompoundInterest() {
        BigDecimal compoundInterest = debt.calculateCompoundInterest();
        assertEquals(new BigDecimal("1102.50"), compoundInterest);
    }

    @Test
    void testGetRemainingAmount() {
        Payment payment = Payment.builder()
                .amount(expectedInstallmentAmount)
                .build();
        debt.getPayments().add(payment);

        BigDecimal remainingAmount = debt.getRemainingAmount();
        BigDecimal expectedRemainingAmount = debt.getTotalAmount().subtract(expectedInstallmentAmount);
        assertEquals(expectedRemainingAmount, remainingAmount);
    }

    @Test
    void testIsOverdue() {
        Installment installment = Installment.builder()
                .dueDate(LocalDate.now().minusDays(1))
                .paid(false)
                .build();
        debt.getInstallments().add(installment);

        assertTrue(debt.isOverdue());
    }

    @Test
    void testUpdateStatus() {
        Installment paidInstallment = Installment.builder()
                .dueDate(LocalDate.now().plusDays(1))
                .paid(true)
                .build();
        Installment unpaidInstallment = Installment.builder()
                .dueDate(LocalDate.now().plusDays(2))
                .paid(false)
                .build();
        debt.getInstallments().add(paidInstallment);
        debt.getInstallments().add(unpaidInstallment);

        debt.updateStatus();
        assertEquals(DebtStatus.PENDING, debt.getStatus());

        unpaidInstallment.setPaid(true);
        debt.getInstallments().forEach(installment -> installment.setPaid(true));
        debt.updateStatus();
        assertEquals(DebtStatus.PAID, debt.getStatus());

        unpaidInstallment.setPaid(false);
        unpaidInstallment.setDueDate(LocalDate.now().minusDays(1));
        debt.updateStatus();
        assertEquals(DebtStatus.OVERDUE, debt.getStatus());
    }

    @Test
    void testPrePersist() {
        debt.prePersist();
        assertEquals(DebtStatus.PENDING, debt.getStatus());
        assertNotNull(debt.getTotalAmount());
        assertFalse(debt.getInstallments().isEmpty());
    }
}
