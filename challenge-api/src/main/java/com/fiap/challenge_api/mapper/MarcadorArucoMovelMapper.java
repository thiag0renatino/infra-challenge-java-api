package com.fiap.challenge_api.mapper;

import com.fiap.challenge_api.dto.MarcadorArucoMovelDTO;
import com.fiap.challenge_api.dto.MarcadorArucoMovelResponseDTO;
import com.fiap.challenge_api.model.MarcadorArucoMovel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MarcadorArucoMovelMapper {

    @Mappings({
            @Mapping(source = "idMarcadorMovel", target = "idMarcadorMovel"),
            @Mapping(source = "moto.idMoto", target = "idMoto")
    })
    MarcadorArucoMovelDTO toDTO(MarcadorArucoMovel entity);

    @Mappings({
            @Mapping(source = "idMarcadorMovel", target = "idMarcadorMovel"),
            @Mapping(source = "idMoto", target = "moto.idMoto")
    })
    MarcadorArucoMovel toEntity(MarcadorArucoMovelDTO dto);

    @Mapping(source = "moto", target = "moto")
    MarcadorArucoMovelResponseDTO toResponseDTO(MarcadorArucoMovel entity);

    @Mapping(source = "moto.idMoto", target = "idMoto")
    MarcadorArucoMovelDTO toDtoFromResponse(MarcadorArucoMovelResponseDTO responseDto);

}
