package com.fiap.challenge_api.controller;

import com.fiap.challenge_api.config.SecurityConfig;
import com.fiap.challenge_api.dto.MedicaoPosicaoDTO;
import com.fiap.challenge_api.service.MedicaoPosicaoService;
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
@RequestMapping("/api/medicoes")
@SecurityRequirement(name = SecurityConfig.SECURITY)
public class MedicaoPosicaoController {

    @Autowired
    MedicaoPosicaoService service;

    @Operation(summary = "Listar todas as medições com paginação",
            description = "Retorna uma lista paginada com todas as medições registradas no sistema")
    @GetMapping
    public ResponseEntity<Page<MedicaoPosicaoDTO>> findAll(@ParameterObject
                                                           @PageableDefault(page = 0, size = 20, sort = "idMedicao", direction = Sort.Direction.DESC)
                                                           Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Buscar medição por ID",
            description = "Retorna os dados de uma medição específica pelo seu ID")
    @GetMapping("/{id}")
    public ResponseEntity<MedicaoPosicaoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Listar medições por posição",
            description = "Retorna todas as medições associadas a uma posição específica")
    @GetMapping("/posicao/{id}")
    public ResponseEntity<List<MedicaoPosicaoDTO>> findByPosicaoIdPosicao(@PathVariable Long id) {
        List<MedicaoPosicaoDTO> medicoes = service.findByPosicaoIdPosicao(id);
        return ResponseEntity.ok(medicoes);
    }

    @Operation(summary = "Listar medições por marcador fixo",
            description = "Retorna todas as medições associadas a um marcador fixo específico")
    @GetMapping("/marcador/{id}")
    public ResponseEntity<List<MedicaoPosicaoDTO>> findByMarcadorFixoId(@PathVariable Long id) {
        List<MedicaoPosicaoDTO> medicoes = service.findByMarcadorFixoId(id);
        return ResponseEntity.ok(medicoes);
    }

    @Operation(summary = "Contar medições por posição",
            description = "Retorna o total de medições registradas para uma determinada posição")
    @GetMapping("/posicao/{id}/contagem")
    public ResponseEntity<Long> contarPorPosicao(@PathVariable Long id) {
        return ResponseEntity.ok(service.contarPorPosicao(id));
    }

    @Operation(summary = "Cadastrar nova medição",
            description = "Cria uma nova medição de posição no sistema")
    @PostMapping
    public ResponseEntity<MedicaoPosicaoDTO> insert(@RequestBody @Valid MedicaoPosicaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(dto));
    }

    @Operation(summary = "Excluir medição",
            description = "Remove uma medição do sistema com base no ID fornecido")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
