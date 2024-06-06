package com.br.celcoin.debtmanagerapi.controller;


import com.br.celcoin.debtmanagerapi.controller.validation.HttpStatusCode;
import com.br.celcoin.debtmanagerapi.model.dto.request.DebtRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.request.DebtSeachRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtResponseDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtSeachResponseDto;
import com.br.celcoin.debtmanagerapi.service.DebtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debts")
@Tag(name = "Debt Management", description = "APIs for managing debts")
public class DebtController {

    private final DebtService debtService;

    @Autowired
    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    @HttpStatusCode
    @PostMapping
    @Operation(summary = "Create a new debt", description = "Creates a new debt record in the system")
    public ResponseEntity<DebtResponseDto> createDebt(@Valid @RequestBody DebtRequestDto debt) {
        return ResponseEntity.ok(debtService.createDebt(debt));
    }

    @HttpStatusCode
    @GetMapping
    @Operation(summary = "Search debts", description = "Search for debts based on various criteria")
    public ResponseEntity<Page<DebtSeachResponseDto>> searchDebts(
            @Parameter(description = "Pagination information") @ParameterObject Pageable pageable,
            @Parameter(description = "Search criteria") @ParameterObject DebtSeachRequestDto dto) {
        return ResponseEntity.ok(debtService.searchDebts(dto, pageable));
    }

    @HttpStatusCode
    @GetMapping("/{id}")
    @Operation(summary = "Get debt by ID", description = "Retrieve a debt record by its ID")
    public ResponseEntity<DebtSeachResponseDto> getDebtById(@Parameter(description = "ID of the debt to be retrieved") @PathVariable Long id) {
        return ResponseEntity.ok(debtService.getDebtById(id));
    }
}
