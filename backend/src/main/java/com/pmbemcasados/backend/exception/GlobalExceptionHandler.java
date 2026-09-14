package com.pmbemcasados.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> recursoNaoEncontrado(
            RecursoNaoEncontradoException ex) {

        return criarResposta(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> regraNegocio(
            RegraNegocioException ex) {

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacao(
            MethodArgumentNotValidException ex) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(erro -> erro.getField() + ": "
                        + erro.getDefaultMessage())
                .orElse("Dados inválidos.");

        return criarResposta(
                HttpStatus.BAD_REQUEST,
                mensagem);
    }

    private ResponseEntity<Map<String, Object>> criarResposta(
            HttpStatus status,
            String mensagem) {

        Map<String, Object> erro = new HashMap<>();

        erro.put("status", status.value());
        erro.put("erro", status.getReasonPhrase());
        erro.put("mensagem", mensagem);
        erro.put("dataHora", LocalDateTime.now());

        return ResponseEntity
                .status(status)
                .body(erro);
    }
}