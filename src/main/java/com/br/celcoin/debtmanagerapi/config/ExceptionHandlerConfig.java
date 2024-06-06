package com.br.celcoin.debtmanagerapi.config;


import com.br.celcoin.debtmanagerapi.exceptions.AllInstallmentsPaidException;
import com.br.celcoin.debtmanagerapi.model.dto.response.ErrorDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException ex) {

        final var codigoRetorno = 404;
        final var defaultErrorDTO = this.buildDefaultErrorRet(codigoRetorno, ex.getMessage(), ex);

        return new ResponseEntity<>(defaultErrorDTO, new HttpHeaders(), NOT_FOUND);
    }

    @ExceptionHandler(ConversionFailedException.class)
    protected ResponseEntity<ErrorDTO> handleConvesionFailedException(ConversionFailedException ex) {

        final var codigoRetorno = 400;
        final var defaultErrorDTO = this.buildDefaultErrorRet(codigoRetorno, "Requisição com parametros inválidos. Falha na conversão.", ex);

        return new ResponseEntity<>(defaultErrorDTO, new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    protected ResponseEntity<ErrorDTO> handleArgumentTypeMismatchException(Exception ex) {
        String mensagemErro;
        String mensagemDefault = "Parametros invalidos: ";
        final var codigoRetorno = 400;
        if (ex instanceof MethodArgumentTypeMismatchException) {
            mensagemErro = "Falha na conversao de tipo para o parametro: " + ((MethodArgumentTypeMismatchException) ex).getName();
        } else {
            mensagemErro = "Erro na validacao do argumento: " + ex.getMessage();
        }
        final var defaultErrorDTO = this.buildDefaultErrorRet(codigoRetorno, mensagemDefault + mensagemErro, ex);
        return new ResponseEntity<>(defaultErrorDTO, new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(AllInstallmentsPaidException.class)
    protected ResponseEntity<ErrorDTO> businessValidationExceptionHandler(AllInstallmentsPaidException ex) {
        final var defaultErrorDTO = this.buildDefaultErrorRet(400, ex.getMessage(), null, ex);

        return new ResponseEntity<>(defaultErrorDTO, new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorDTO> badRequestExceptionHandler(BadRequestException ex) {

        final var codigoRetorno = 400;
        final var defaultErrorDTO = this.buildDefaultErrorRet(codigoRetorno, ex.getMessage(), ex);

        return new ResponseEntity<>(defaultErrorDTO, new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleException(Exception ex) {
        return this.internalExceptionHandler(ex);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public final ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex) {
        return this.internalExceptionHandler(ex);
    }


    private ResponseEntity<Object> internalExceptionHandler(Exception ex) {
        var errorDTO = new ErrorDTO();

        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setException(ex.getClass().getSimpleName());

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    private ErrorDTO buildDefaultErrorRet(final Integer codigoRetorno, String mensagem, Exception exception) {
        var defaultErrorDTO = new ErrorDTO();
        defaultErrorDTO.setMensagem(mensagem);
        defaultErrorDTO.setException(exception.getClass().getSimpleName());
        return defaultErrorDTO;
    }


    private ErrorDTO buildDefaultErrorRet(final Integer codigoRetorno, String mensagem, Map<String, List<String>> errors, Exception exception) {
        final var defaultErrorDTO = buildDefaultErrorRet(codigoRetorno, mensagem, exception);
        defaultErrorDTO.setErrors(errors);
        return defaultErrorDTO;
    }

}
