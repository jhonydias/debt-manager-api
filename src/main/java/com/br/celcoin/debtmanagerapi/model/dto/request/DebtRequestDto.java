package com.br.celcoin.debtmanagerapi.model.dto.request;

import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.br.celcoin.debtmanagerapi.model.entity.Debt}
 */
@Schema(description = "DTO for creating a debt")
public record DebtRequestDto(

        @NotNull(message = "Principal amount is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Principal amount must be greater than zero")
        @Schema(description = "Principal amount of the debt", example = "1000.00")
        BigDecimal principalAmount,

        @NotNull(message = "Interest rate is mandatory")
        @DecimalMin(value = "0.0", inclusive = false, message = "Interest rate must be greater than zero")
        @Schema(description = "Interest rate for the debt", example = "0.05")
        BigDecimal interestRate,

        @NotBlank(message = "Creditor name is mandatory")
        @Schema(description = "Name of the creditor", example = "Creditor Name")
        String creditorName,

//        @NotNull(message = "Due date is mandatory")
//        @Future(message = "Due date must be in the future")
//        @Schema(description = "Due date of the debt", example = "2024-12-31")
//        LocalDate dueDate,

        @NotNull(message = "Number of installments is mandatory")
        @Min(value = 1, message = "Number of installments must be at least 1")
        @Schema(description = "Number of installments for the debt", example = "6")
        Integer numberOfInstallments

) implements Serializable {
    public Debt toEntity() {
        return Debt.builder()
                .principalAmount(principalAmount)
                .creditorName(creditorName)
                .dueDate(LocalDate.now().plusMonths(1))
                .numberOfInstallments(numberOfInstallments)
                .interestRate(interestRate)
                .build();
    }
}
