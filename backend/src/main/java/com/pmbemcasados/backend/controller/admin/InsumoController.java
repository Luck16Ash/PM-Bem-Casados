package com.pmbemcasados.backend.controller.admin;

import com.pmbemcasados.backend.dto.InsumoRequest;
import com.pmbemcasados.backend.dto.InsumoResponse;
import com.pmbemcasados.backend.service.InsumoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/insumos")
@RequiredArgsConstructor
public class InsumoController {

    private final InsumoService service;

    @GetMapping
    public List<InsumoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public InsumoResponse buscar(
            @PathVariable UUID id) {

        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsumoResponse criar(
            @Valid @RequestBody InsumoRequest request) {

        return service.criar(request);
    }
}