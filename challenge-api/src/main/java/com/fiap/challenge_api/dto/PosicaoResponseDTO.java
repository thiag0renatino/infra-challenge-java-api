package com.fiap.challenge_api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "idPosicao", "xpos", "ypos", "dataHora", "moto", "patio" })
public class PosicaoResponseDTO extends RepresentationModel<PosicaoResponseDTO> {

    private Long idPosicao;
    private Float xPos;
    private Float yPos;
    private LocalDateTime dataHora;

    private MotoDTO moto;
    private PatioDTO patio;

    public Float getYPos() {
        return yPos;
    }

    public void setYPos(Float yPos) {
        this.yPos = yPos;
    }

    public Float getXPos() {
        return xPos;
    }

    public void setXPos(Float xPos) {
        this.xPos = xPos;
    }

    public Long getIdPosicao() {
        return idPosicao;
    }

    public void setIdPosicao(Long idPosicao) {
        this.idPosicao = idPosicao;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public MotoDTO getMoto() {
        return moto;
    }

    public void setMoto(MotoDTO moto) {
        this.moto = moto;
    }

    public PatioDTO getPatio() {
        return patio;
    }

    public void setPatio(PatioDTO patio) {
        this.patio = patio;
    }
}
