package com.br.celcoin.debtmanagerapi.controller;


import com.br.celcoin.debtmanagerapi.model.dto.request.DebtRequestDto;
import com.br.celcoin.debtmanagerapi.model.dto.response.DebtResponseDto;
import com.br.celcoin.debtmanagerapi.model.entity.Debt;
import com.br.celcoin.debtmanagerapi.service.DebtService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/debts")
public class DebtController {

    private final DebtService debtService;

    @Autowired
    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    @PostMapping
    public ResponseEntity<DebtResponseDto> createDebt(@Valid @RequestBody DebtRequestDto debt) {
        return ResponseEntity.ok(debtService.createDebt(debt));
    }

    @GetMapping
    public ResponseEntity<Page<Debt>> getAllDebts(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(debtService.getAllDebts(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getDebtById(@PathVariable Long id) {
        Debt debt = debtService.getDebtById(id);
        return debt != null ? ResponseEntity.ok(debt) : ResponseEntity.notFound().build();
    }

}
