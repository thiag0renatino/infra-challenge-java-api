package com.fiap.challenge_api.repository;

import com.fiap.challenge_api.model.MedicaoPosicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicaoPosicaoRepository extends JpaRepository<MedicaoPosicao, Long> {

    List<MedicaoPosicao> findByPosicaoIdPosicao(Long posicaoId);

    List<MedicaoPosicao> findByMarcadorFixoIdMarcadorArucoFixo(Long marcadorFixoId);

    @Query("SELECT COUNT(m) FROM MedicaoPosicao m WHERE m.posicao.idPosicao = :id")
    Long contarMedicoesPorPosicao(@Param("id") Long idPosicao);

}
