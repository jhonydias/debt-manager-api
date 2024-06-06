package com.br.celcoin.debtmanagerapi.model.entity;

import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Debt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount; // Total value of the debt plus interest
    private BigDecimal principalAmount; // Value of the debt without interest
    private String creditorName;
    private LocalDate dueDate; // Date when the debt is due
    private Integer numberOfInstallments;
    private BigDecimal interestRate;
    @Enumerated(EnumType.STRING)
    private DebtStatus status;
    private LocalDate lastPaymentDate;

    @OneToMany(mappedBy = "debt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "debt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Installment> installments = new ArrayList<>();

    public void generateInstallments() {
        if (installments == null) {
            installments = new ArrayList<>();
        }
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_UP);
        for (int i = 0; i < numberOfInstallments; i++) {
            Installment installment = new Installment();
            installment.setDebt(this);
            installment.setAmount(installmentAmount);
            installment.setDueDate(dueDate.plusMonths(i));
            installment.setPaid(false);
            installments.add(installment);
        }
    }

    public BigDecimal calculateCompoundInterest() {
        if (this.principalAmount != null && this.interestRate != null && this.numberOfInstallments != null) {
            BigDecimal onePlusRate = BigDecimal.ONE.add(this.interestRate);
            BigDecimal compoundFactor = onePlusRate.pow(this.numberOfInstallments);
            return this.principalAmount.multiply(compoundFactor).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getRemainingAmount() {
        BigDecimal paidAmount = payments != null ? payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;
        return totalAmount != null ? totalAmount.subtract(paidAmount) : BigDecimal.ZERO;
    }

    public boolean isOverdue() {
        return installments.stream().anyMatch(Installment::isOverdue);
    }

    public void updateStatus() {
        if (installments.stream().allMatch(Installment::isPaid)) {
            this.status = DebtStatus.PAID;
        } else if (installments.stream().anyMatch(Installment::isOverdue)) {
            this.status = DebtStatus.OVERDUE;
        } else {
            this.status = DebtStatus.PENDING;
        }
    }


    @PrePersist
    public void prePersist() {
        this.status = DebtStatus.PENDING;
        this.totalAmount = calculateCompoundInterest(); // Use compound interest for total amount calculation
        generateInstallments();
    }
}
