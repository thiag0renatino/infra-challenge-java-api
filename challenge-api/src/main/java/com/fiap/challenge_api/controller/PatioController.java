package com.fiap.challenge_api.controller;

import com.fiap.challenge_api.config.SecurityConfig;
import com.fiap.challenge_api.dto.MotoDTO;
import com.fiap.challenge_api.dto.PatioDTO;
import com.fiap.challenge_api.dto.PosicaoDTO;
import com.fiap.challenge_api.dto.PosicaoResponseDTO;
import com.fiap.challenge_api.service.MotoService;
import com.fiap.challenge_api.service.PatioService;
import com.fiap.challenge_api.service.PosicaoService;
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
@RequestMapping("/api/patios")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class PatioController {

    @Autowired
    private PatioService service;

    @Autowired
    private MotoService motoService;

    @Autowired
    private PosicaoService posicaoService;

    @Operation(summary = "Listar todos os pátios com paginação",
            description = "Retorna uma lista paginada com todos os pátios cadastrados")
    @GetMapping
    public ResponseEntity<Page<PatioDTO>> findAll(@ParameterObject
                                                  @PageableDefault(page = 0, size = 20, sort = "idPatio", direction = Sort.Direction.DESC)
                                                  Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Buscar pátio por ID",
            description = "Retorna um pátio específico com base no ID informado")
    @GetMapping("/{id}")
    public ResponseEntity<PatioDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar pátios com motos",
            description = "Retorna apenas os pátios que possuem motos cadastradas")
    @GetMapping("/com-motos")
    public ResponseEntity<List<PatioDTO>> findPatiosComMotos() {
        List<PatioDTO> patios = service.findPatiosComMotos();
        return ResponseEntity.ok(patios);
    }

    @Operation(summary = "Listar motos com histórico de passagem pelo pátio",
            description = "Retorna todas as motos que já tiveram uma posição registrada neste pátio")
    @GetMapping("/{id}/motos")
    public ResponseEntity<List<MotoDTO>> findMotosPorPatio(@PathVariable Long id) {
        return ResponseEntity.ok(motoService.findMotosPorHistoricoDePosicaoNoPatio(id));
    }

    @Operation(summary = "Listar posições registradas no pátio",
            description = "Retorna todas as posições que foram registradas dentro do pátio informado")
    @GetMapping("/{id}/posicoes")
    public ResponseEntity<List<PosicaoResponseDTO>> findPosicoesPorPatio(@PathVariable Long id) {
        return ResponseEntity.ok(posicaoService.findByPatioId(id));
    }


    @Operation(summary = "Cadastrar novo pátio",
            description = "Cria um novo pátio no sistema com os dados fornecidos")
    @PostMapping
    public ResponseEntity<PatioDTO> insert(@RequestBody @Valid PatioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(dto));
    }

    @Operation(summary = "Atualizar pátio",
            description = "Atualiza os dados de um pátio existente com base no ID fornecido")
    @PutMapping("/{id}")
    public ResponseEntity<PatioDTO> update(@PathVariable Long id, @RequestBody @Valid PatioDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Excluir pátio",
            description = "Remove um pátio do sistema com base no ID informado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
