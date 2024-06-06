package com.br.celcoin.debtmanagerapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "debt_id", nullable = false)
    private Debt debt;

    private BigDecimal amount;
    private LocalDate dueDate;
    private boolean paid;

    public boolean isOverdue() {
        return !paid && LocalDate.now().isAfter(dueDate);
    }

}
