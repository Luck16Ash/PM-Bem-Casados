package com.pmbemcasados.backend.controller.publicapi;

import com.pmbemcasados.backend.dto.ProdutoResponse;
import com.pmbemcasados.backend.dto.TomCorResponse;
import com.pmbemcasados.backend.service.ProdutoService;
import com.pmbemcasados.backend.service.TomCorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogoController {

    private final ProdutoService produtoService;
    private final TomCorService tomCorService;

    @GetMapping("/produtos")
    public List<ProdutoResponse> produtos() {
        return produtoService.listarAtivos();
    }

    @GetMapping("/tons-cor")
    public List<TomCorResponse> tons() {
        return tomCorService.listarAtivos();
    }
}