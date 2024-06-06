package com.br.celcoin.debtmanagerapi.model.dto.response;

import com.br.celcoin.debtmanagerapi.model.entity.Payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.br.celcoin.debtmanagerapi.model.entity.Payment}
 */
public record PaymentResponseDto(LocalDateTime dateCreate, LocalDateTime dateUpdate, Long id, BigDecimal amount,
                                 LocalDate paymentDate) implements Serializable {
    public static PaymentResponseDto fromEntity(Payment payment) {
        return new PaymentResponseDto(payment.getDateCreate(), payment.getDateUpdate(), payment.getId(),
                payment.getAmount(), payment.getPaymentDate());
    }
}