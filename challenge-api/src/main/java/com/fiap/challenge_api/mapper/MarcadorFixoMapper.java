package com.fiap.challenge_api.mapper;

import com.fiap.challenge_api.dto.MarcadorFixoDTO;
import com.fiap.challenge_api.dto.MarcadorFixoResponseDTO;
import com.fiap.challenge_api.model.MarcadorFixo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MarcadorFixoMapper {

    @Mappings({
            @Mapping(source = "patio.idPatio", target = "idPatio"),
            @Mapping(source = "idMarcadorArucoFixo", target = "idMarcadorArucoFixo")
    })
    MarcadorFixoDTO toDTO(MarcadorFixo entity);

    @Mappings({
            @Mapping(source = "idPatio", target = "patio.idPatio"),
            @Mapping(source = "idMarcadorArucoFixo", target = "idMarcadorArucoFixo")
    })
    MarcadorFixo toEntity(MarcadorFixoDTO dto);

    @Mapping(source = "patio", target = "patio")
    MarcadorFixoResponseDTO toResponseDTO(MarcadorFixo entity);

}
