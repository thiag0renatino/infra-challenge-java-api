package com.fiap.challenge_api.mapper;

import com.fiap.challenge_api.dto.MedicaoPosicaoDTO;
import com.fiap.challenge_api.model.MedicaoPosicao;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicaoPosicaoMapper {

    @Mappings({
            @Mapping(source = "idMedicao", target = "idMedicao"),
            @Mapping(source = "posicao.idPosicao", target = "idPosicao"),
            @Mapping(source = "marcadorFixo.idMarcadorArucoFixo", target = "idMarcadorFixo")
    })
    MedicaoPosicaoDTO toDTO(MedicaoPosicao entity);

    @Mappings({
            @Mapping(source = "idMedicao", target = "idMedicao"),
            @Mapping(source = "idPosicao", target = "posicao.idPosicao"),
            @Mapping(source = "idMarcadorFixo", target = "marcadorFixo.idMarcadorArucoFixo")
    })
    MedicaoPosicao toEntity(MedicaoPosicaoDTO dto);

    List<MedicaoPosicaoDTO> toDtoList(List<MedicaoPosicao> entities);

}
