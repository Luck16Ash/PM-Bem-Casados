package com.pmbemcasados.backend.service;

import com.pmbemcasados.backend.domain.Cliente;
import com.pmbemcasados.backend.dto.ClienteRequest;
import com.pmbemcasados.backend.dto.ClienteResponse;
import com.pmbemcasados.backend.exception.RecursoNaoEncontradoException;
import com.pmbemcasados.backend.exception.RegraNegocioException;
import com.pmbemcasados.backend.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponse> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(this::converter)
                .toList();
    }

    public ClienteResponse buscar(UUID id) {
        return converter(buscarEntidade(id));
    }

    public ClienteResponse criar(ClienteRequest request) {

        if (!Boolean.TRUE.equals(request.consentimentoLgpd())) {
            throw new RegraNegocioException(
                    "O consentimento LGPD é obrigatório.");
        }

        if (clienteRepository.findByWhatsapp(request.whatsapp()).isPresent()) {
            throw new RegraNegocioException(
                    "Já existe um cliente com esse WhatsApp.");
        }

        if (request.email() != null
                && !request.email().isBlank()
                && clienteRepository.findByEmail(request.email()).isPresent()) {

            throw new RegraNegocioException(
                    "Já existe um cliente com esse e-mail.");
        }

        Cliente cliente = Cliente.builder()
                .nome(request.nome())
                .whatsapp(request.whatsapp())
                .email(normalizarEmail(request.email()))
                .consentimentoLgpdEm(LocalDateTime.now())
                .build();

        return converter(clienteRepository.save(cliente));
    }

    public ClienteResponse atualizar(
            UUID id,
            ClienteRequest request) {

        Cliente cliente = buscarEntidade(id);

        cliente.setNome(request.nome());
        cliente.setWhatsapp(request.whatsapp());
        cliente.setEmail(normalizarEmail(request.email()));

        return converter(clienteRepository.save(cliente));
    }

    public Cliente buscarEntidade(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado."));
    }

    private String normalizarEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        return email.trim().toLowerCase();
    }

    private ClienteResponse converter(Cliente cliente) {

        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getWhatsapp(),
                cliente.getEmail());
    }
}