package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.exceptions.AllInstallmentsPaidException;
import com.br.celcoin.debtmanagerapi.model.dto.request.PaymentRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.InstallmentResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.PaymentResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.entity.Installment;
import com.br.celcoin.debtmanagerapi.model.entity.Payment;
import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import com.br.celcoin.debtmanagerapi.repository.InstallmentRepository;
import com.br.celcoin.debtmanagerapi.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private InstallmentRepository installmentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Captor
    private ArgumentCaptor<Installment> installmentCaptor;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterPayment_Success() {
        Long debtId = 1L;
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(debtId);

        Debt debt = createDebt(debtId);
        Installment installment = createInstallment(debt);
        Payment payment = createPayment(debt, installment);

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));
        when(installmentRepository.save(any(Installment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });
        when(debtRepository.save(any(Debt.class))).thenReturn(debt);

        PaymentResponseDto response = paymentService.registerPayment(paymentRequestDto);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(payment.getAmount(), response.amount());
        assertEquals(payment.getPaymentDate(), response.paymentDate());

        verify(debtRepository, times(1)).findById(debtId);
        verify(installmentRepository, times(1)).save(installmentCaptor.capture());
        verify(paymentRepository, times(1)).save(paymentCaptor.capture());
        verify(debtRepository, times(1)).save(debt);

        Installment savedInstallment = installmentCaptor.getValue();
        assertTrue(savedInstallment.isPaid());

        Payment savedPayment = paymentCaptor.getValue();
        assertEquals(installment.getAmount(), savedPayment.getAmount());
        assertEquals(debt, savedPayment.getDebt());
    }

    @Test
    void testRegisterPayment_AllInstallmentsPaid() {
        Long debtId = 1L;
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(debtId);

        Debt debt = createDebt(debtId);
        debt.getInstallments().forEach(i -> i.setPaid(true));

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));

        assertThrows(AllInstallmentsPaidException.class, () -> paymentService.registerPayment(paymentRequestDto));

        verify(debtRepository, times(1)).findById(debtId);
        verifyNoMoreInteractions(installmentRepository, paymentRepository, debtRepository);
    }

    @Test
    void testGetNextInstallmentToPay_Success() {
        Long debtId = 1L;
        Debt debt = createDebt(debtId);
        Installment installment = debt.getInstallments().get(0);

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));

        InstallmentResponseDto response = paymentService.getNextInstallmentToPay(debtId);

        assertNotNull(response);
        assertEquals(installment.getId(), response.id());
        assertEquals(installment.getAmount(), response.amount());
        assertEquals(installment.getDueDate(), response.dueDate());

        verify(debtRepository, times(1)).findById(debtId);
    }

    @Test
    void testGetNextInstallmentToPay_AllInstallmentsPaid() {
        Long debtId = 1L;
        Debt debt = createDebt(debtId);
        debt.getInstallments().forEach(i -> i.setPaid(true));

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));

        assertThrows(AllInstallmentsPaidException.class, () -> paymentService.getNextInstallmentToPay(debtId));

        verify(debtRepository, times(1)).findById(debtId);
    }

    @Test
    void testFindById_Success() {
        Long debtId = 1L;
        Debt debt = createDebt(debtId);

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));

        Debt foundDebt = paymentService.findById(debtId);

        assertNotNull(foundDebt);
        assertEquals(debt.getId(), foundDebt.getId());

        verify(debtRepository, times(1)).findById(debtId);
    }

    @Test
    void testFindById_NotFound() {
        Long debtId = 1L;

        when(debtRepository.findById(debtId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.findById(debtId));

        verify(debtRepository, times(1)).findById(debtId);
    }

    private Debt createDebt(Long debtId) {
        Debt debt = Debt.builder()
                .id(debtId)
                .principalAmount(new BigDecimal("1000.00"))
                .totalAmount(new BigDecimal("1200.00"))
                .creditorName("Creditor")
                .dueDate(LocalDate.now().plusMonths(1))
                .numberOfInstallments(2)
                .interestRate(new BigDecimal("0.05"))
                .status(DebtStatus.PENDING)
                .lastPaymentDate(null)
                .payments(new ArrayList<>())
                .installments(new ArrayList<>())
                .build();

        Installment installment1 = Installment.builder()
                .id(1L)
                .amount(new BigDecimal("600.00"))
                .dueDate(LocalDate.now().plusMonths(1))
                .paid(false)
                .debt(debt)
                .build();

        Installment installment2 = Installment.builder()
                .id(2L)
                .amount(new BigDecimal("600.00"))
                .dueDate(LocalDate.now().plusMonths(2))
                .paid(false)
                .debt(debt)
                .build();

        debt.getInstallments().add(installment1);
        debt.getInstallments().add(installment2);
        return debt;
    }

    private Installment createInstallment(Debt debt) {
        return Installment.builder()
                .id(1L)
                .amount(new BigDecimal("600.00"))
                .dueDate(LocalDate.now().plusMonths(1))
                .paid(false)
                .debt(debt)
                .build();
    }

    private Payment createPayment(Debt debt, Installment installment) {
        return Payment.builder()
                .id(1L)
                .amount(installment.getAmount())
                .paymentDate(LocalDate.now())
                .debt(debt)
                .build();
    }
}
