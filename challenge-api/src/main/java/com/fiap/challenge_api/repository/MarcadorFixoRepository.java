package com.fiap.challenge_api.repository;

import com.fiap.challenge_api.model.MarcadorFixo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarcadorFixoRepository extends JpaRepository<MarcadorFixo, Long> {

    List<MarcadorFixo> findByPatioIdPatio(Long patioId);

    Optional<MarcadorFixo> findByCodigoArucoIgnoreCase(String codigoAruco);
}
