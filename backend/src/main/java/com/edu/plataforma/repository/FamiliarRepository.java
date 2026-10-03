package com.edu.plataforma.repository;

import com.edu.plataforma.model.Familiar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FamiliarRepository extends JpaRepository<Familiar, Long> {
    Optional<Familiar> findByUsuarioId(Long usuarioId);
    Optional<Familiar> findByUsuarioCorreo(String correo);

    @Query("SELECT v.familiar FROM VinculoFamiliarEstudiante v WHERE v.estudiante.id = :estudianteId")
    List<Familiar> findByEstudianteId(@Param("estudianteId") Long estudianteId);
}