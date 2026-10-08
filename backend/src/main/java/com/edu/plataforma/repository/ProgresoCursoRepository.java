package com.edu.plataforma.repository;

import com.edu.plataforma.model.ProgresoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoCursoRepository extends JpaRepository<ProgresoCurso, Long> {
    List<ProgresoCurso> findByCursoId(Long cursoId);
    List<ProgresoCurso> findByEstudianteId(Long estudianteId);
    Optional<ProgresoCurso> findByEstudianteIdAndCursoId(Long estudianteId, Long cursoId);
}
