package com.pmbemcasados.backend.controller.admin;

import com.pmbemcasados.backend.dto.ProdutoRequest;
import com.pmbemcasados.backend.dto.ProdutoResponse;
import com.pmbemcasados.backend.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @GetMapping
    public List<ProdutoResponse> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(
            @PathVariable UUID id) {

        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse criar(
            @Valid @RequestBody ProdutoRequest request) {

        return service.criar(request);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ProdutoRequest request) {

        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(
            @PathVariable UUID id) {

        service.inativar(id);
    }
}