package com.br.celcoin.debtmanagerapi.model.entity;

import com.br.celcoin.debtmanagerapi.model.enums.DebtStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public BigDecimal getRemainingAmount() {
        BigDecimal paidAmount = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalAmount.subtract(paidAmount);
    }
}
