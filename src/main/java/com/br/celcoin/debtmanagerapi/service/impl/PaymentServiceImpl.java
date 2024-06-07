package com.br.celcoin.debtmanagerapi.service.impl;

import com.br.celcoin.debtmanagerapi.exceptions.AllInstallmentsPaidException;
import com.br.celcoin.debtmanagerapi.model.dto.request.PaymentRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.InstallmentResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.PaymentResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.model.entity.Installment;
import com.br.celcoin.debtmanagerapi.model.entity.Payment;
import com.br.celcoin.debtmanagerapi.repository.DebtRepository;
import com.br.celcoin.debtmanagerapi.repository.InstallmentRepository;
import com.br.celcoin.debtmanagerapi.repository.PaymentRepository;
import com.br.celcoin.debtmanagerapi.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final DebtRepository debtRepository;
    private final InstallmentRepository installmentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              DebtRepository debtRepository,
                              InstallmentRepository installmentRepository) {
        this.installmentRepository = installmentRepository;
        this.debtRepository = debtRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Register a payment for a given debt and update related information.
     *
     * @param dto The payment request DTO containing necessary information.
     * @return {@link PaymentResponseDto} The payment response DTO after processing the payment.
     */
    @Override
    @Transactional
    public PaymentResponseDto registerPayment(PaymentRequestDto dto) {
        final Debt debt = findById(dto.idDebt());

        final Installment installment = findNextUnpaidInstallment(debt);

        installment.setPaid(true);
        installmentRepository.save(installment);

        debt.updateStatus();
        debt.setLastPaymentDate(LocalDate.now());
        debtRepository.save(debt);

        final Payment payment = Payment.builder()
                .paymentDate(LocalDate.now())
                .amount(installment.getAmount())
                .debt(debt)
                .build();
        paymentRepository.save(payment);
        return PaymentResponseDto.fromEntity(payment);
    }

    @Override
    public List<InstallmentResponseDto> getInstallments(Long debtId) {
        final Debt debt = findById(debtId);
        return debt.getInstallments().stream()
                .map(InstallmentResponseDto::fromEntity)
                .toList();
    }

    @Override
    public InstallmentResponseDto getNextInstallmentToPay(Long debtId) {
        final Debt debt = findById(debtId);
        Optional<Installment> nextInstallment = debt.getInstallments().stream()
                .filter(installment -> !installment.isPaid() && installment.getDueDate().isAfter(LocalDate.now()))
                .findFirst();
        return nextInstallment
                .map(InstallmentResponseDto::fromEntity)
                .orElseThrow(() -> new AllInstallmentsPaidException("All installments are paid"));
    }

    public Debt findById(Long id) {
        return debtRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Debt not found. Id: " + id)
        );
    }

    private Installment findNextUnpaidInstallment(Debt debt) {
        return debt.getInstallments().stream()
                .filter(installment -> !installment.isPaid())
                .findFirst()
                .orElseThrow(() -> new AllInstallmentsPaidException("All installments are paid"));
    }
}
