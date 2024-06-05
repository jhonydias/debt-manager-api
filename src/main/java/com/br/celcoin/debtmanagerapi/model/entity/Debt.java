package com.br.celcoin.debtmanagerapi.model.entity;

import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    private BigDecimal totalAmount; // Valor total da dívida
    private BigDecimal principalAmount; // Valor principal da dívida
    private String creditorName;
    private LocalDate dueDate; // Data de vencimento
    private Integer numberOfInstallments; // Quantidade de parcelas
    private BigDecimal interestRate; // Taxa de juros anual
    @Enumerated(EnumType.STRING)
    private DebtStatus status;

    @OneToMany(mappedBy = "debt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    public BigDecimal calculateInterest() {
        if (this.principalAmount != null && this.interestRate != null && this.numberOfInstallments != null) {
            BigDecimal onePlusRate = BigDecimal.ONE.add(this.interestRate);
            BigDecimal compoundFactor = onePlusRate.pow(this.numberOfInstallments);
            return this.principalAmount.multiply(compoundFactor);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getRemainingAmount() {
        BigDecimal paidAmount = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalAmount.subtract(paidAmount);
    }

    public boolean isOverdue() {
        LocalDate today = LocalDate.now();
        return today.isAfter(dueDate) && getRemainingAmount().compareTo(BigDecimal.ZERO) > 0;
    }

    @PrePersist
    public void prePersist() {
        this.status = DebtStatus.PENDING;
        this.totalAmount = calculateInterest();
    }
}
