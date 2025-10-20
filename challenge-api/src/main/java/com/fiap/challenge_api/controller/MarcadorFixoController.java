package com.fiap.challenge_api.controller;

import com.fiap.challenge_api.config.SecurityConfig;
import com.fiap.challenge_api.dto.MarcadorFixoDTO;
import com.fiap.challenge_api.dto.MarcadorFixoResponseDTO;
import com.fiap.challenge_api.service.MarcadorFixoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcadores-fixos")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class MarcadorFixoController {

    @Autowired
    private MarcadorFixoService service;

    @Operation(summary = "Listar todos os marcadores fixos com paginação",
            description = "Retorna uma lista paginada com todos os marcadores ArUco fixos cadastrados")
    @GetMapping
    public ResponseEntity<Page<MarcadorFixoResponseDTO>> findALl(@ParameterObject
                                                                 @PageableDefault(page = 0, size = 20, sort = "idMarcadorArucoFixo", direction = Sort.Direction.DESC)
                                                                 Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Listar marcadores fixos por pátio",
            description = "Retorna todos os marcadores fixos associados a um pátio específico")
    @GetMapping("/por-patio/{patioId}")
    public ResponseEntity<List<MarcadorFixoResponseDTO>> findByPatioId(@PathVariable Long patioId) {
        return ResponseEntity.ok(service.findByPatioId(patioId));
    }

    @Operation(summary = "Buscar marcador fixo por código ArUco",
            description = "Retorna um marcador fixo a partir do código ArUco fornecido")
    @GetMapping("/por-codigo")
    public ResponseEntity<MarcadorFixoResponseDTO> findByCodigoAruco(@RequestParam String codigoAruco) {
        return ResponseEntity.ok(service.findByCodigoAruco(codigoAruco));
    }

    @Operation(summary = "Cadastrar novo marcador fixo",
            description = "Cria um novo marcador ArUco fixo no sistema")
    @PostMapping
    public ResponseEntity<MarcadorFixoResponseDTO> insert(@RequestBody @Valid MarcadorFixoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(dto));
    }

    @Operation(summary = "Excluir marcador fixo",
            description = "Remove um marcador fixo com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
