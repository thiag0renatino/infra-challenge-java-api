package com.fiap.challenge_api.service;

import com.fiap.challenge_api.controller.MarcadorFixoController;
import com.fiap.challenge_api.dto.MarcadorFixoDTO;
import com.fiap.challenge_api.dto.MarcadorFixoResponseDTO;
import com.fiap.challenge_api.mapper.MarcadorFixoMapper;
import com.fiap.challenge_api.model.MarcadorFixo;
import com.fiap.challenge_api.repository.MarcadorFixoRepository;
import com.fiap.challenge_api.repository.PatioRepository;
import com.fiap.challenge_api.service.exception.MarcadorNotFoundException;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MarcadorFixoService {

    @Autowired
    private MarcadorFixoRepository repository;

    @Autowired
    private PatioRepository patioRepository;

    @Autowired
    private MarcadorFixoMapper mapper;

    public Page<MarcadorFixoResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> {
                    MarcadorFixoResponseDTO dto = mapper.toResponseDTO(entity);
                    addHateoasLinks(dto);
                    return dto;
                });
    }

    public MarcadorFixoDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public MarcadorFixoResponseDTO findByIdResponse(Long id) {
        MarcadorFixoResponseDTO dto = repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        addHateoasLinks(dto);
        return dto;
    }

    public List<MarcadorFixoResponseDTO> findByPatioId(Long patioId) {
        List<MarcadorFixoResponseDTO> marcadores = repository.findByPatioIdPatio(patioId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();

        marcadores.forEach(MarcadorFixoService::addHateoasLinks);
        return marcadores;
    }

    public MarcadorFixoResponseDTO findByCodigoAruco(String codigoAruco) {
        MarcadorFixo marcadorFixo = repository.findByCodigoArucoIgnoreCase(codigoAruco)
                .orElseThrow(() -> new MarcadorNotFoundException(codigoAruco));
        MarcadorFixoResponseDTO response = mapper.toResponseDTO(marcadorFixo);
        addHateoasLinks(response);
        return response;
    }

    public MarcadorFixoResponseDTO insert(MarcadorFixoDTO dto) {
        MarcadorFixo marcadorFixo = mapper.toEntity(dto);

        var patio = patioRepository.findById(dto.getIdPatio())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getIdPatio()));

        marcadorFixo.setPatio(patio);

        MarcadorFixo saved = repository.save(marcadorFixo);

        MarcadorFixoResponseDTO response = mapper.toResponseDTO(saved);
        addHateoasLinks(response);
        return response;
    }

    public void delete(Long id) {
        MarcadorFixo marcadorFixo = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(marcadorFixo);
    }

    private static void addHateoasLinks(MarcadorFixoResponseDTO dto) {
        var pageableExemplo = PageRequest.of(0, 20, Sort.by("idMarcadorArucoFixo").descending());
        dto.add(linkTo(methodOn(MarcadorFixoController.class).findALl(pageableExemplo)).withRel("findAll").withType("GET"));

        if (dto.getCodigoAruco() != null) {
            WebMvcLinkBuilder selfLink = linkTo(methodOn(MarcadorFixoController.class).findByCodigoAruco(dto.getCodigoAruco()));
            dto.add(selfLink.withSelfRel().withType("GET"));
            dto.add(selfLink.withRel("findByCodigoAruco").withType("GET"));
        }
        if (dto.getPatio() != null && dto.getPatio().getIdPatio() != null) {
            dto.add(linkTo(methodOn(MarcadorFixoController.class).findByPatioId(dto.getPatio().getIdPatio())).withRel("findByPatioId").withType("GET"));
        }
        dto.add(linkTo(methodOn(MarcadorFixoController.class).insert(null)).withRel("create").withType("POST"));
        if (dto.getIdMarcadorArucoFixo() != null) {
            dto.add(linkTo(methodOn(MarcadorFixoController.class).delete(dto.getIdMarcadorArucoFixo())).withRel("delete").withType("DELETE"));
        }
    }

}
