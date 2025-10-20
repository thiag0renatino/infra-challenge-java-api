package com.fiap.challenge_api.mapper;

import com.fiap.challenge_api.dto.UsuarioDTO;
import com.fiap.challenge_api.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {


    @Mapping(source = "patio.idPatio", target = "patioId")
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(source = "patioId", target = "patio.idPatio")
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
