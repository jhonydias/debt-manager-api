package com.br.celcoin.debtmanagerapi.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class InstallmentTest {

    private Installment installment;

    @BeforeEach
    public void setUp() {
        installment = Installment.builder()
                .amount(new BigDecimal("600.00"))
                .dueDate(LocalDate.now().plusMonths(1))
                .paid(false)
                .build();
    }

    @Test
    void testIsOverdue_NotOverdue() {
        installment.setDueDate(LocalDate.now().plusDays(1));
        assertFalse(installment.isOverdue());
    }

    @Test
    void testIsOverdue_Overdue() {
        installment.setDueDate(LocalDate.now().minusDays(1));
        assertTrue(installment.isOverdue());
    }

    @Test
    void testIsOverdue_Paid() {
        installment.setDueDate(LocalDate.now().minusDays(1));
        installment.setPaid(true);
        assertFalse(installment.isOverdue());
    }

    @Test
    void testGettersAndSetters() {
        installment.setId(1L);
        installment.setDebt(new Debt());
        installment.setAmount(new BigDecimal("500.00"));
        installment.setDueDate(LocalDate.now().plusMonths(2));
        installment.setPaid(true);

        assertEquals(1L, installment.getId());
        assertNotNull(installment.getDebt());
        assertEquals(new BigDecimal("500.00"), installment.getAmount());
        assertEquals(LocalDate.now().plusMonths(2), installment.getDueDate());
        assertTrue(installment.isPaid());
    }
}
