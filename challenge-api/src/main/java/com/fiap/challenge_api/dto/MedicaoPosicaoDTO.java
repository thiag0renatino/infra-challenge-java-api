package com.fiap.challenge_api.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MedicaoPosicaoDTO extends RepresentationModel<MedicaoPosicaoDTO> {

    private Long idMedicao;
    private Double distanciaM;
    private Long idPosicao;
    private Long idMarcadorFixo;

    public Long getIdMedicao() {
        return idMedicao;
    }

    public void setIdMedicao(Long idMedicao) {
        this.idMedicao = idMedicao;
    }

    public Long getIdPosicao() {
        return idPosicao;
    }

    public void setIdPosicao(Long idPosicao) {
        this.idPosicao = idPosicao;
    }

    public Long getIdMarcadorFixo() {
        return idMarcadorFixo;
    }

    public void setIdMarcadorFixo(Long idMarcadorFixo) {
        this.idMarcadorFixo = idMarcadorFixo;
    }

    public Double getDistanciaM() {
        return distanciaM;
    }

    public void setDistanciaM(Double distanciaM) {
        this.distanciaM = distanciaM;
    }
}
