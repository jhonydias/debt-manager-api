package com.br.celcoin.debtmanagerapi.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String mensagem;
    private Map<String, List<String>> errors = new HashMap<>();
    private String exception;
}


