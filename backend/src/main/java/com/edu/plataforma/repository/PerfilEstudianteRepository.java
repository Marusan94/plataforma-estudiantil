package com.edu.plataforma.repository;

import com.edu.plataforma.model.PerfilEstudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilEstudianteRepository extends JpaRepository<PerfilEstudiante, Long> {
    Optional<PerfilEstudiante> findByEstudianteId(Long estudianteId);
    boolean existsByEstudianteId(Long estudianteId);

    @Query("SELECT p FROM PerfilEstudiante p WHERE LOWER(p.intereses) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.resumen) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<PerfilEstudiante> buscarPorPalabraClave(@Param("query") String query);

    @Query("SELECT p FROM PerfilEstudiante p JOIN p.estudiante e WHERE (:programa IS NULL OR e.programa = :programa) AND (:semestre IS NULL OR e.semestre = :semestre)")
    Page<PerfilEstudiante> filtrarPerfiles(@Param("programa") String programa, @Param("semestre") Integer semestre, Pageable pageable);
}