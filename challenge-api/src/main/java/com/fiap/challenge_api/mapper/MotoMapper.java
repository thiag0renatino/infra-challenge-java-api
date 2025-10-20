package com.fiap.challenge_api.mapper;

import com.fiap.challenge_api.dto.MotoDTO;
import com.fiap.challenge_api.model.Moto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MotoMapper {

    MotoDTO toDTO(Moto entity);
    Moto toEntity(MotoDTO dto);

}
