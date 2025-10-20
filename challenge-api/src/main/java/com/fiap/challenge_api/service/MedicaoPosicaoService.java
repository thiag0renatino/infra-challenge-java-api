package com.fiap.challenge_api.service;

import com.fiap.challenge_api.controller.MedicaoPosicaoController;
import com.fiap.challenge_api.dto.MedicaoPosicaoDTO;
import com.fiap.challenge_api.mapper.MedicaoPosicaoMapper;
import com.fiap.challenge_api.model.MedicaoPosicao;
import com.fiap.challenge_api.repository.MedicaoPosicaoRepository;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MedicaoPosicaoService {

    @Autowired
    private MedicaoPosicaoRepository repository;

    @Autowired
    private MedicaoPosicaoMapper mapper;

    @Cacheable("medicoes")
    public Page<MedicaoPosicaoDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> {
                    MedicaoPosicaoDTO dto = mapper.toDTO(entity);
                    addHateoasLinks(dto);
                    return dto;
                });
    }

    public MedicaoPosicaoDTO findById(Long id) {
        MedicaoPosicaoDTO dto = repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        addHateoasLinks(dto);
        return dto;
    }

    public List<MedicaoPosicaoDTO> findByPosicaoIdPosicao(Long idPosicao) {
        List<MedicaoPosicaoDTO> dtos = repository.findByPosicaoIdPosicao(idPosicao)
                .stream()
                .map(mapper::toDTO)
                .toList();

        dtos.forEach(MedicaoPosicaoService::addHateoasLinks);
        return dtos;
    }

    public List<MedicaoPosicaoDTO> findByMarcadorFixoId(Long idMarcadorFixo) {
        List<MedicaoPosicaoDTO> dtos = repository.findByMarcadorFixoIdMarcadorArucoFixo(idMarcadorFixo)
                .stream()
                .map(mapper::toDTO)
                .toList();

        dtos.forEach(MedicaoPosicaoService::addHateoasLinks);
        return dtos;
    }

    public Long contarPorPosicao(Long idPosicao) {
        return repository.contarMedicoesPorPosicao(idPosicao);
    }

    @CacheEvict(value = "medicoes", allEntries = true)
    public MedicaoPosicaoDTO insert(MedicaoPosicaoDTO dto) {
        MedicaoPosicao entity = mapper.toEntity(dto);
        MedicaoPosicao saved = repository.save(entity);
        MedicaoPosicaoDTO response = mapper.toDTO(saved);
        addHateoasLinks(response);
        return response;
    }

    @CacheEvict(value = "medicoes", allEntries = true)
    public void delete(Long id) {
        MedicaoPosicao medicaoPosicao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(medicaoPosicao);
    }

    private static void addHateoasLinks(MedicaoPosicaoDTO dto) {
        var pageableExemplo = PageRequest.of(0, 20, Sort.by("idMedicao").descending());
        dto.add(linkTo(methodOn(MedicaoPosicaoController.class).findAll(pageableExemplo)).withRel("findAll").withType("GET"));
        if (dto.getIdMedicao() != null) {
            dto.add(linkTo(methodOn(MedicaoPosicaoController.class).findById(dto.getIdMedicao())).withSelfRel().withType("GET"));
            dto.add(linkTo(methodOn(MedicaoPosicaoController.class).delete(dto.getIdMedicao())).withRel("delete").withType("DELETE"));
        }

        Long idPosicao = dto.getIdPosicao();
        dto.add(linkTo(methodOn(MedicaoPosicaoController.class).findByPosicaoIdPosicao(idPosicao)).withRel("findByPosicaoIdPosicao").withType("GET"));
        dto.add(linkTo(methodOn(MedicaoPosicaoController.class).contarPorPosicao(idPosicao)).withRel("countByPosicao").withType("GET"));

        if (dto.getIdMarcadorFixo() != null) {
            Long idMarcador = dto.getIdMarcadorFixo();
            dto.add(linkTo(methodOn(MedicaoPosicaoController.class).findByMarcadorFixoId(idMarcador)).withRel("findByMarcadorFixoId").withType("GET"));
        }
        dto.add(linkTo(methodOn(MedicaoPosicaoController.class).insert(null)).withRel("create").withType("POST"));
    }

}
