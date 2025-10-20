package com.fiap.challenge_api.mapper;

import com.fiap.challenge_api.dto.PosicaoDTO;
import com.fiap.challenge_api.dto.PosicaoResponseDTO;
import com.fiap.challenge_api.model.Posicao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PosicaoMapper {

    @Mappings({
            @Mapping(source = "moto.idMoto", target = "idMoto"),
            @Mapping(source = "patio.idPatio", target = "idPatio")
    })
    PosicaoDTO toDTO(Posicao posicao);
    @Mappings({
            @Mapping(source = "idMoto", target = "moto.idMoto"),
            @Mapping(source = "idPatio", target = "patio.idPatio")
    })
    Posicao toEntity(PosicaoDTO dto);

    @Mapping(source = "moto", target = "moto")
    @Mapping(source = "patio", target = "patio")
    PosicaoResponseDTO toResponseDTO(Posicao posicao);

}
