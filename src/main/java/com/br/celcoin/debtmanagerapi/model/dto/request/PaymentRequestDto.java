package com.br.celcoin.debtmanagerapi.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.br.celcoin.debtmanagerapi.model.entity.Payment}
 */
@Schema(description = "DTO for registering a payment")
public record PaymentRequestDto(
        @NotNull(message = "Debt id is mandatory")
        @Schema(description = "Id of the debt", example = "1")
        Long idDebt

) implements Serializable {
}
