package com.fiap.challenge_api.service;

import com.fiap.challenge_api.controller.PatioController;
import com.fiap.challenge_api.controller.PosicaoController;
import com.fiap.challenge_api.dto.MotoDTO;
import com.fiap.challenge_api.dto.PosicaoDTO;
import com.fiap.challenge_api.dto.PosicaoResponseDTO;
import com.fiap.challenge_api.mapper.MotoMapper;
import com.fiap.challenge_api.mapper.PosicaoMapper;
import com.fiap.challenge_api.model.Moto;
import com.fiap.challenge_api.model.Posicao;
import com.fiap.challenge_api.repository.MotoRepository;
import com.fiap.challenge_api.repository.PatioRepository;
import com.fiap.challenge_api.repository.PosicaoRepository;
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
public class PosicaoService {

    @Autowired
    private PosicaoRepository repository;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private PatioRepository patioRepository;

    @Autowired
    private PosicaoMapper mapper;

    @Autowired
    private MotoMapper motoMapper;

    @Cacheable("posicoes")
    public Page<PosicaoResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(entity -> {
                    PosicaoResponseDTO dto = mapper.toResponseDTO(entity);
                    addHateoasLinks(dto);
                    return dto;
                });
    }

    public PosicaoResponseDTO findByIdResponse(Long id) {
        PosicaoResponseDTO dto = repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        addHateoasLinks(dto);
        return dto;
    }

    public PosicaoDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<PosicaoResponseDTO> findByMotoId(Long motoId) {
        List<PosicaoResponseDTO> dtos = repository.findByMoto_IdMoto(motoId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
        dtos.forEach(PosicaoService::addHateoasLinks);
        return dtos;
    }

    public List<PosicaoResponseDTO> findTop10ByOrderByDataHoraDesc() {
        List<PosicaoResponseDTO> dtos = repository.findTop10ByOrderByDataHoraDesc()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
        dtos.forEach(PosicaoService::addHateoasLinks);
        return dtos;
    }

    public List<PosicaoResponseDTO> buscarHistoricoDaMoto(Long motoId) {
        List<PosicaoResponseDTO> dtos = repository.buscarHistoricoDaMoto(motoId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
        dtos.forEach(PosicaoService::addHateoasLinks);
        return dtos;
    }

    public List<PosicaoResponseDTO> findPosicoesDeMotosRevisao() {
        List<PosicaoResponseDTO> dtos = repository.findPosicoesDeMotosRevisao()
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
        dtos.forEach(PosicaoService::addHateoasLinks);
        return dtos;
    }

    public List<PosicaoResponseDTO> findByPatioId(Long patioId) {
        List<PosicaoResponseDTO> dtos = repository.findByPatioIdPatio(patioId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
        dtos.forEach(PosicaoService::addHateoasLinks);
        return dtos;
    }

    public MotoDTO buscarMotoPorIdPosicao(Long posicaoId) {
        Posicao posicao = repository.findById(posicaoId)
                .orElseThrow(() -> new ResourceNotFoundException(posicaoId));

        Moto moto = posicao.getMoto();
        return motoMapper.toDTO(moto);
    }

    @CacheEvict(value = "posicoes", allEntries = true)
    public PosicaoResponseDTO update(Long id, PosicaoDTO dto) {
        Posicao posicaoExist = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        posicaoExist.setXPos(dto.getXPos());
        posicaoExist.setYPos(dto.getYPos());
        posicaoExist.setDataHora(dto.getDataHora());

        if (dto.getIdMoto() != null) {
            var moto = motoRepository.findById(dto.getIdMoto())
                    .orElseThrow(() -> new ResourceNotFoundException(dto.getIdMoto()));
            posicaoExist.setMoto(moto);
        }

        if (dto.getIdPatio() != null) {
            var patio = patioRepository.findById(dto.getIdPatio())
                    .orElseThrow(() -> new ResourceNotFoundException(dto.getIdPatio()));
            posicaoExist.setPatio(patio);
        }

        Posicao saved = repository.save(posicaoExist);

        PosicaoResponseDTO resp = mapper.toResponseDTO(saved);
        addHateoasLinks(resp);
        return resp;
    }

    @CacheEvict(value = "posicoes", allEntries = true)
    public PosicaoResponseDTO insert(PosicaoDTO dto) {
        Posicao posicao = mapper.toEntity(dto);

        var moto = motoRepository.findById(dto.getIdMoto())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getIdMoto()));
        var patio = patioRepository.findById(dto.getIdPatio())
                .orElseThrow(() -> new ResourceNotFoundException(dto.getIdPatio()));

        posicao.setMoto(moto);
        posicao.setPatio(patio);

        if (posicao.getDataHora() == null) {
            posicao.setDataHora(java.time.LocalDateTime.now());
        }

        Posicao saved = repository.save(posicao);

        PosicaoResponseDTO resp = mapper.toResponseDTO(saved);
        addHateoasLinks(resp);
        return resp;

    }

    @CacheEvict(value = "posicoes", allEntries = true)
    public void delete(Long id) {
        Posicao posicao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(posicao);
    }

    private static void addHateoasLinks(PosicaoResponseDTO dto) {
        var pageableExemplo = PageRequest.of(0, 20, Sort.by("idPosicao").descending());
        dto.add(linkTo(methodOn(PosicaoController.class).findAll(pageableExemplo)).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PosicaoController.class).findTop10ByOrderByDataHoraDesc()).withRel("findTop10ByOrderByDataHoraDesc").withType("GET"));
        dto.add(linkTo(methodOn(PosicaoController.class).findPosicoesDeMotosRevisao()).withRel("findPosicoesDeMotosRevisao").withType("GET"));
        if (dto.getIdPosicao() != null) {
            dto.add(linkTo(methodOn(PosicaoController.class).buscarMotoPorPosicao(dto.getIdPosicao())).withRel("buscarMotoPorPosicao").withType("GET"));
            dto.add(linkTo(methodOn(PosicaoController.class).update(dto.getIdPosicao(), new PosicaoDTO())).withRel("update").withType("PUT"));
            dto.add(linkTo(methodOn(PosicaoController.class).delete(dto.getIdPosicao())).withRel("delete").withType("DELETE"));
        }

        if (dto.getMoto() != null && dto.getMoto().getIdMoto() != null) {
            dto.add(linkTo(methodOn(PosicaoController.class).findByMotoId(dto.getMoto().getIdMoto())).withRel("findByMotoId").withType("GET"));
            dto.add(
                    linkTo(methodOn(PosicaoController.class).buscarHistoricoDaMoto(dto.getMoto().getIdMoto())).withRel("buscarHistoricoDaMoto").withType("GET"));
        }
        if (dto.getPatio() != null && dto.getPatio().getIdPatio() != null) {
            dto.add(linkTo(methodOn(PatioController.class).findPosicoesPorPatio(dto.getPatio().getIdPatio())).withRel("findByPatioId").withType("GET")
            );
        }
        dto.add(linkTo(methodOn(PosicaoController.class).insert(new PosicaoDTO())).withRel("create").withType("POST"));
    }
}
