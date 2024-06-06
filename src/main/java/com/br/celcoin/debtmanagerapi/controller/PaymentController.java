package com.br.celcoin.debtmanagerapi.controller;


import com.br.celcoin.debtmanagerapi.controller.validation.HttpStatusCode;
import com.br.celcoin.debtmanagerapi.model.dto.request.PaymentRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.InstallmentResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.PaymentResponseDto;
import com.br.celcoin.debtmanagerapi.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payment Management", description = "APIs for managing payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @HttpStatusCode
    @PostMapping
    @Operation(summary = "Register a payment", description = "Registers a new payment for a debt")
    public ResponseEntity<PaymentResponseDto> registerPayment(@Valid @RequestBody PaymentRequestDto dto) {
        return ResponseEntity.ok(paymentService.registerPayment(dto));
    }

    @HttpStatusCode
    @Operation(summary = "Get installments", description = "Retrieve installments for a given debt ID")
    @GetMapping("/installments")
    public ResponseEntity<List<InstallmentResponseDto>> getInstallments(@RequestParam Long debtId) {
        return ResponseEntity.ok(paymentService.getInstallments(debtId));
    }

    @HttpStatusCode
    @Operation(summary = "Get next installment to pay", description = "Retrieve the next installment to be paid for a given payment ID")
    @GetMapping("/{id}/next-installment")
    public ResponseEntity<InstallmentResponseDto> getNextInstallmentToPay(@PathVariable Long id) {
        InstallmentResponseDto nextInstallment = paymentService.getNextInstallmentToPay(id);
        return ResponseEntity.ok(nextInstallment);
    }
}
