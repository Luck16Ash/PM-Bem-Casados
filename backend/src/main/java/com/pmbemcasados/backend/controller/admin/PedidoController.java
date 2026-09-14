package com.pmbemcasados.backend.controller.admin;

import com.pmbemcasados.backend.dto.PedidoResponse;
import com.pmbemcasados.backend.dto.PedidoStatusRequest;
import com.pmbemcasados.backend.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public List<PedidoResponse> listar() {
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(
            @PathVariable UUID id) {

        return pedidoService.buscar(id);
    }

    @PatchMapping("/{id}/status")
    public PedidoResponse alterarStatus(
            @PathVariable UUID id,
            @Valid @RequestBody PedidoStatusRequest request) {

        return pedidoService.alterarStatus(
                id,
                request);
    }
}