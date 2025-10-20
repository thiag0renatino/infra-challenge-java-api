package com.fiap.challenge_api.service;

import com.fiap.challenge_api.controller.MarcadorArucoMovelController;
import com.fiap.challenge_api.dto.MarcadorArucoMovelDTO;
import com.fiap.challenge_api.dto.MarcadorArucoMovelResponseDTO;
import com.fiap.challenge_api.mapper.MarcadorArucoMovelMapper;
import com.fiap.challenge_api.model.MarcadorArucoMovel;
import com.fiap.challenge_api.repository.MarcadorArucoMovelRepository;
import com.fiap.challenge_api.repository.MotoRepository;
import com.fiap.challenge_api.service.exception.MarcadorNotFoundException;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MarcadorArucoMovelService {

    @Autowired
    private MarcadorArucoMovelRepository repository;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private MarcadorArucoMovelMapper mapper;

    public Page<MarcadorArucoMovelResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> {
                    MarcadorArucoMovelResponseDTO dto = mapper.toResponseDTO(entity);
                    addHateoasLinks(dto);
                    return dto;
                });
    }

    public MarcadorArucoMovelResponseDTO findById(Long id) {
        MarcadorArucoMovel entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        MarcadorArucoMovelResponseDTO dto = mapper.toResponseDTO(entity);
        addHateoasLinks(dto);
        return dto;
    }

    public Page<MarcadorArucoMovelResponseDTO> findByMotoId(Long motoId, Pageable pageable) {
        return repository.findByMoto_IdMoto(motoId, pageable)
                .map(entity -> {
                    MarcadorArucoMovelResponseDTO dto = mapper.toResponseDTO(entity);
                    addHateoasLinks(dto);
                    return dto;
                });
    }

    public MarcadorArucoMovelResponseDTO findByCodigoAruco(String codigo) {
        MarcadorArucoMovelResponseDTO dto = repository.findByCodigoArucoIgnoreCase(codigo)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new MarcadorNotFoundException(codigo));
        addHateoasLinks(dto);
        return dto;
    }

    public MarcadorArucoMovelResponseDTO insert(MarcadorArucoMovelDTO dto) {
        MarcadorArucoMovel marcador = mapper.toEntity(dto);

        var moto = motoRepository.findById(dto.getIdMoto())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getIdMoto()));

        marcador.setMoto(moto);
        marcador.setDataInstalacao(LocalDate.now());

        // Salva entidade no banco
        MarcadorArucoMovel saved = repository.save(marcador);

        // Adiciona hateoas no DTO de resposta
        MarcadorArucoMovelResponseDTO response = mapper.toResponseDTO(saved);
        addHateoasLinks(response);

        return response;
    }

    public MarcadorArucoMovelResponseDTO update(Long id, MarcadorArucoMovelDTO dto) {
        MarcadorArucoMovel marcadorExist = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (dto.getCodigoAruco() != null) {
            marcadorExist.setCodigoAruco(dto.getCodigoAruco());
        }
        if (dto.getDataInstalacao() != null) {
            marcadorExist.setDataInstalacao(dto.getDataInstalacao());
        }

        if (dto.getIdMoto() != null) {
            var moto = motoRepository.findById(dto.getIdMoto())
                    .orElseThrow(() -> new ResourceNotFoundException(dto.getIdMoto()));
            marcadorExist.setMoto(moto);
        }

        MarcadorArucoMovel marcadorAtt = repository.save(marcadorExist);

        MarcadorArucoMovelResponseDTO response = mapper.toResponseDTO(marcadorAtt);
        addHateoasLinks(response);

        return response;
    }

    public void delete(Long id) {
        MarcadorArucoMovel marcadorArucoMovel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(marcadorArucoMovel);
    }

    private static void addHateoasLinks(MarcadorArucoMovelResponseDTO dto) {
        var pageableExemplo = PageRequest.of(0, 20, Sort.by("idMarcadorMovel").ascending());
        dto.add(linkTo(methodOn(MarcadorArucoMovelController.class).findAll(pageableExemplo)).withRel("findAll").withType("GET"));
        if (dto.getIdMarcadorMovel() != null) {
            dto.add(linkTo(methodOn(MarcadorArucoMovelController.class).findById(dto.getIdMarcadorMovel())).withSelfRel().withType("GET"));
        }
        if (dto.getMoto() != null && dto.getMoto().getIdMoto() != null) {
            dto.add(linkTo(methodOn(MarcadorArucoMovelController.class).findByMotoId(dto.getMoto().getIdMoto(), pageableExemplo)).withRel("findByMotoId").withType("GET"));
        }
        if (dto.getCodigoAruco() != null) {
            dto.add(linkTo(methodOn(MarcadorArucoMovelController.class).findByCodigoAruco(dto.getCodigoAruco())).withRel("findByCodigoAruco").withType("GET"));
        }
        dto.add(linkTo(methodOn(MarcadorArucoMovelController.class).insert(null)).withRel("create").withType("POST"));
        if (dto.getIdMarcadorMovel() != null) {
            dto.add(linkTo(methodOn(MarcadorArucoMovelController.class).update(dto.getIdMarcadorMovel(), new MarcadorArucoMovelDTO())).withRel("update").withType("PUT"));
        }
        if (dto.getIdMarcadorMovel() != null) {
            dto.add(linkTo(methodOn(MarcadorArucoMovelController.class).delete(dto.getIdMarcadorMovel())).withRel("delete").withType("DELETE"));
        }
    }
}
