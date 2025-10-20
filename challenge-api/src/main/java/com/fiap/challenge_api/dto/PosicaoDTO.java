package com.fiap.challenge_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.boot.convert.DataSizeUnit;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PosicaoDTO {

    private Long idPosicao;
    private Float xPos;
    private Float yPos;

    private LocalDateTime dataHora;

    private Long idMoto;
    private Long idPatio;

    public Long getIdPosicao() {
        return idPosicao;
    }

    public void setIdPosicao(Long idPosicao) {
        this.idPosicao = idPosicao;
    }

    public Long getIdPatio() {
        return idPatio;
    }

    public void setIdPatio(Long idPatio) {
        this.idPatio = idPatio;
    }

    public Long getIdMoto() {
        return idMoto;
    }

    public void setIdMoto(Long idMoto) {
        this.idMoto = idMoto;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

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
}
