package com.pmbemcasados.backend.controller.admin;

import com.pmbemcasados.backend.dto.TomCorRequest;
import com.pmbemcasados.backend.dto.TomCorResponse;
import com.pmbemcasados.backend.service.TomCorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/tons-cor")
@RequiredArgsConstructor
public class TomCorController {

    private final TomCorService service;

    @GetMapping
    public List<TomCorResponse> listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TomCorResponse criar(
            @Valid @RequestBody TomCorRequest request) {

        return service.criar(request);
    }

    @PutMapping("/{id}")
    public TomCorResponse atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody TomCorRequest request) {

        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(
            @PathVariable UUID id) {

        service.inativar(id);
    }
}