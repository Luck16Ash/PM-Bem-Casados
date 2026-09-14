package com.pmbemcasados.backend.controller.admin;

import com.pmbemcasados.backend.dto.FornecedorRequest;
import com.pmbemcasados.backend.dto.FornecedorResponse;
import com.pmbemcasados.backend.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService service;

    @GetMapping
    public List<FornecedorResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public FornecedorResponse buscar(
            @PathVariable UUID id) {

        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FornecedorResponse criar(
            @Valid @RequestBody FornecedorRequest request) {

        return service.criar(request);
    }
}