package com.fiap.challenge_api.repository;

import com.fiap.challenge_api.model.MarcadorArucoMovel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarcadorArucoMovelRepository extends JpaRepository<MarcadorArucoMovel, Long> {

    Page<MarcadorArucoMovel> findByMoto_IdMoto(Long motoId, Pageable pageable);

    Optional<MarcadorArucoMovel> findByCodigoArucoIgnoreCase(String codigoAruco);
}
