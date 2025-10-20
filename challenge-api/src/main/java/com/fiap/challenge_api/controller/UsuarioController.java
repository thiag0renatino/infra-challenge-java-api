package com.fiap.challenge_api.controller;

import com.fiap.challenge_api.config.SecurityConfig;
import com.fiap.challenge_api.dto.AtualizarSenhaRequestDTO;
import com.fiap.challenge_api.dto.UsuarioDTO;
import com.fiap.challenge_api.service.AuthService;
import com.fiap.challenge_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private AuthService authService;

    @Operation(summary = "Listar todos os usuários",
            description = "Retorna uma lista com todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar usuário por ID",
            description = "Retorna um usuário específico com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Buscar usuário por e-mail",
            description = "Retorna o usuário correspondente ao e-mail informado")
    @GetMapping("/por-email")
    public ResponseEntity<UsuarioDTO> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @Operation(summary = "Atualiza senha do usuário",
            description = "Atualiza a senha de um usuário ao passar a antiga corretamente")
    @PatchMapping("/atualizar-senha")
    public ResponseEntity<Void> atualizarSenha(@RequestBody @Valid AtualizarSenhaRequestDTO dto, Principal connectedUser) {
        authService.changePassword(dto, connectedUser);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizar usuário",
            description = "Atualiza as informações de um usuário existente com base no ID informado")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @RequestBody @Valid UsuarioDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Excluir usuário",
            description = "Remove um usuário do sistema com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
