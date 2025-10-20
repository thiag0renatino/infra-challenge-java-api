package com.fiap.challenge_api.mapper;

import com.fiap.challenge_api.dto.PatioDTO;
import com.fiap.challenge_api.model.Patio;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatioMapper {

    PatioDTO toDTO(Patio patio);
    Patio toEntity(PatioDTO dto);

}
