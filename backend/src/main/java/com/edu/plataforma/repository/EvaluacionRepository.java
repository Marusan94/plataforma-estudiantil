package com.edu.plataforma.repository;

import com.edu.plataforma.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByCursoId(Long cursoId);
}
