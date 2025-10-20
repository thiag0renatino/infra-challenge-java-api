package com.fiap.challenge_api.service;

import com.fiap.challenge_api.controller.MotoController;
import com.fiap.challenge_api.dto.MotoDTO;
import com.fiap.challenge_api.mapper.MotoMapper;
import com.fiap.challenge_api.model.Moto;
import com.fiap.challenge_api.repository.MotoRepository;
import com.fiap.challenge_api.service.exception.PlacaInvalidaException;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MotoService {

    @Autowired
    private MotoRepository repository;

    @Autowired
    private MotoMapper mapper;

    private static final String PLACA_PATTERN = ("^(?:[A-Z]{3}\\d[A-Z0-9]\\d{2}|[A-Z]{3}-?\\d{4})$");

    public Page<MotoDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> {
                    MotoDTO dto = mapper.toDTO(entity);
                    addHateoasLinks(dto);
                    return dto;
                });
    }

    public MotoDTO findById(Long id) {
        Moto moto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        MotoDTO dto = mapper.toDTO(moto);
        addHateoasLinks(dto);
        return dto;
    }

    public List<MotoDTO> findByPlaca(String placa) {
        List<MotoDTO> dtos = repository.findByPlacaStartsWithIgnoreCase(placa)
                .stream()
                .map(mapper::toDTO)
                .toList();

        dtos.forEach(MotoService::addHateoasLinks);
        return dtos;
    }

    public List<MotoDTO> findByStatus(String status) {
        List<MotoDTO> dtos = repository.findByStatusIgnoreCase(status)
                .stream()
                .map(mapper::toDTO)
                .toList();

        dtos.forEach(MotoService::addHateoasLinks);
        return dtos;
    }

    public List<MotoDTO> findMotosPorHistoricoDePosicaoNoPatio(Long patioId) {
        List<MotoDTO> dtos = repository.findMotosQuePassaramPorPatio(patioId)
                .stream()
                .map(mapper::toDTO)
                .toList();

        dtos.forEach(MotoService::addHateoasLinks);
        return dtos;
    }

    public MotoDTO insert(MotoDTO dto) {
        if (!dto.getPlaca().matches(PLACA_PATTERN)) {
            throw new PlacaInvalidaException("Placa inválida. O padrão deve ser ABC1D23.");
        }
        Moto moto = mapper.toEntity(dto);
        moto.setDataCadastro(LocalDate.now());

        MotoDTO resp = mapper.toDTO(repository.save(moto));
        addHateoasLinks(resp);
        return resp;
    }

    public MotoDTO update(Long id, MotoDTO dto) {
        Moto motoExist = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        if (!dto.getPlaca().matches(PLACA_PATTERN)) {
            throw new PlacaInvalidaException("Placa inválida. O padrão deve ser ABC1D23.");
        }

        motoExist.setPlaca(dto.getPlaca());
        motoExist.setModelo(dto.getModelo());
        motoExist.setStatus(dto.getStatus());

        MotoDTO resp = mapper.toDTO(repository.save(motoExist));
        addHateoasLinks(resp);
        return resp;
    }

    public void delete(Long id) {
        Moto moto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(moto);
    }

    private static void addHateoasLinks(MotoDTO dto) {
        var pageableExemplo = PageRequest.of(0, 20, Sort.by("idMoto").descending());
        dto.add(linkTo(methodOn(MotoController.class).findAll(pageableExemplo)).withRel("findAll").withType("GET"));
        if (dto.getPlaca() != null && !dto.getPlaca().isBlank()) {
            dto.add(linkTo(methodOn(MotoController.class).findByPlaca(dto.getPlaca())).withRel("findByPlaca").withType("GET"));
        }
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            dto.add(linkTo(methodOn(MotoController.class).findByStatus(dto.getStatus())).withRel("findByStatus").withType("GET"));
        }
        if (dto.getIdMoto() != null) {
            dto.add(linkTo(methodOn(MotoController.class).findById(dto.getIdMoto())).withSelfRel().withType("GET"));

            dto.add(linkTo(methodOn(MotoController.class).findPosicoesPorMoto(dto.getIdMoto())).withRel("findPosicoesByMoto").withType("GET"));

            dto.add(linkTo(methodOn(MotoController.class).update(dto.getIdMoto(), new MotoDTO())).withRel("update").withType("PUT"));

            dto.add(linkTo(methodOn(MotoController.class).delete(dto.getIdMoto())).withRel("delete").withType("DELETE"));
        }

        dto.add(linkTo(methodOn(MotoController.class).insert(new MotoDTO())).withRel("create").withType("POST"));
    }
}
