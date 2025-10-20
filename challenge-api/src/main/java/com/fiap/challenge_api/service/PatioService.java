package com.fiap.challenge_api.service;

import com.fiap.challenge_api.controller.PatioController;
import com.fiap.challenge_api.dto.PatioDTO;
import com.fiap.challenge_api.mapper.PatioMapper;
import com.fiap.challenge_api.model.Patio;
import com.fiap.challenge_api.repository.PatioRepository;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PatioService {

    @Autowired
    private PatioRepository repository;

    @Autowired
    private PatioMapper mapper;

    public Page<PatioDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> {
                    PatioDTO dto = mapper.toDTO(entity);
                    addHateoasLinks(dto);
                    return dto;
                });
    }

    public PatioDTO findById(Long id) {
        PatioDTO dto = repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        addHateoasLinks(dto);
        return dto;
    }

    public List<PatioDTO> findPatiosComMotos() {
        List<PatioDTO> dtos = repository.findPatiosComMotos()
                .stream()
                .map(mapper::toDTO)
                .toList();

        dtos.forEach(PatioService::addHateoasLinks);
        return dtos;
    }

    public PatioDTO insert(PatioDTO dto) {
        Patio patio = mapper.toEntity(dto);
        PatioDTO saved = mapper.toDTO(repository.save(patio));
        addHateoasLinks(saved);
        return saved;
    }

    public PatioDTO update(Long id, PatioDTO dto) {
        Patio patioExist = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        patioExist.setNome(dto.getNome());
        patioExist.setLocalizacao(dto.getLocalizacao());
        patioExist.setDescricao(dto.getDescricao());

        PatioDTO resp = mapper.toDTO(repository.save(patioExist));
        addHateoasLinks(resp);
        return resp;
    }

    public void delete(Long id) {
        Patio patio = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(patio);
    }

    private static void addHateoasLinks(PatioDTO dto) {
        var pageableExemplo = PageRequest.of(0, 20, Sort.by("idPatio").descending());
        dto.add(linkTo(methodOn(PatioController.class).findAll(pageableExemplo)).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PatioController.class).findPatiosComMotos()).withRel("findPatiosComMotos").withType("GET"));
        if (dto.getIdPatio() != null) {
            dto.add(linkTo(methodOn(PatioController.class).findById(dto.getIdPatio())).withSelfRel().withType("GET"));
            dto.add(linkTo(methodOn(PatioController.class).findMotosPorPatio(dto.getIdPatio())).withRel("findMotosPorPatio").withType("GET"));
            dto.add(linkTo(methodOn(PatioController.class).findPosicoesPorPatio(dto.getIdPatio())).withRel("findPosicoesPorPatio").withType("GET"));
            dto.add(linkTo(methodOn(PatioController.class).update(dto.getIdPatio(), new PatioDTO())).withRel("update").withType("PUT"));
            dto.add(linkTo(methodOn(PatioController.class).delete(dto.getIdPatio())).withRel("delete").withType("DELETE"));
        }
        dto.add(linkTo(methodOn(PatioController.class).insert(new PatioDTO())).withRel("create").withType("POST"));
    }
}
