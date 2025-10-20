package com.fiap.challenge_api.controller;

import com.fiap.challenge_api.config.SecurityConfig;
import com.fiap.challenge_api.dto.MarcadorArucoMovelDTO;
import com.fiap.challenge_api.dto.MarcadorArucoMovelResponseDTO;
import com.fiap.challenge_api.service.MarcadorArucoMovelService;
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

@RestController
@RequestMapping("/api/marcadores-moveis")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class MarcadorArucoMovelController {

    @Autowired
    private MarcadorArucoMovelService service;

    @Operation(summary = "Listar todos os marcadores móveis",
            description = "Retorna uma lista com todos os marcadores ArUco móveis cadastrados")
    @GetMapping
    public ResponseEntity<Page<MarcadorArucoMovelResponseDTO>> findAll(@ParameterObject
                                                                       @PageableDefault(page = 0, size = 20, sort = "idMarcadorMovel", direction = Sort.Direction.DESC)
                                                                       Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Buscar marcador móvel por ID",
            description = "Retorna os dados de um marcador móvel específico pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<MarcadorArucoMovelResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Buscar marcador móvel por ID da moto",
            description = "Retorna o marcador móvel vinculado a uma moto específica")
    @GetMapping("/por-moto")
    public ResponseEntity<Page<MarcadorArucoMovelResponseDTO>> findByMotoId(@RequestParam Long idMoto,
                                                                            @PageableDefault(page = 0, size = 20, sort = "idMarcadorMovel", direction = Sort.Direction.DESC)
                                                                            Pageable pageable) {
        return ResponseEntity.ok(service.findByMotoId(idMoto, pageable));
    }

    @Operation(summary = "Buscar marcador móvel por código ArUco",
            description = "Retorna um marcador móvel a partir do código ArUco fornecido")
    @GetMapping("/por-codigo")
    public ResponseEntity<MarcadorArucoMovelResponseDTO> findByCodigoAruco(@RequestParam String codigoAruco) {
        return ResponseEntity.ok(service.findByCodigoAruco(codigoAruco));
    }

    @Operation(summary = "Cadastrar novo marcador móvel",
            description = "Cria um novo marcador ArUco móvel no sistema")
    @PostMapping
    public ResponseEntity<MarcadorArucoMovelResponseDTO> insert(@RequestBody @Valid MarcadorArucoMovelDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(dto));
    }

    @Operation(summary = "Atualizar marcador móvel",
            description = "Atualiza os dados de um marcador móvel com base no ID fornecido")
    @PutMapping("/{id}")
    public ResponseEntity<MarcadorArucoMovelResponseDTO> update(@PathVariable Long id, @RequestBody @Valid MarcadorArucoMovelDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Excluir marcador móvel",
            description = "Remove um marcador móvel com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
