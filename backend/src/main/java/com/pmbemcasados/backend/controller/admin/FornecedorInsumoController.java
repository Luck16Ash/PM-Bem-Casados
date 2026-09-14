package com.pmbemcasados.backend.controller.admin;

import com.pmbemcasados.backend.dto.FornecedorInsumoRequest;
import com.pmbemcasados.backend.dto.FornecedorInsumoResponse;
import com.pmbemcasados.backend.service.FornecedorInsumoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/fornecedores-insumos")
@RequiredArgsConstructor
public class FornecedorInsumoController {

    private final FornecedorInsumoService service;

    @GetMapping
    public List<FornecedorInsumoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public FornecedorInsumoResponse buscar(
            @PathVariable UUID id) {

        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FornecedorInsumoResponse criar(
            @Valid @RequestBody FornecedorInsumoRequest request) {

        return service.criar(request);
    }

    @PutMapping("/{id}")
    public FornecedorInsumoResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody FornecedorInsumoRequest request) {

        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(
            @PathVariable UUID id) {

        service.excluir(id);
    }
}