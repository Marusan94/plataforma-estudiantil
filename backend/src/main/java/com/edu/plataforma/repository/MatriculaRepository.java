package com.edu.plataforma.repository;

import com.edu.plataforma.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByEstudianteId(Long estudianteId);
    List<Matricula> findByGrupoId(Long grupoId);
    Optional<Matricula> findByEstudianteIdAndMateriaIdAndPeriodo(Long estudianteId, Long materiaId, String periodo);
}