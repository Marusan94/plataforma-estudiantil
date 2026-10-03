package com.edu.plataforma.repository;

import com.edu.plataforma.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByUsuarioId(Long usuarioId);
    Optional<Estudiante> findByCodigoEstudiante(String codigoEstudiante);
    List<Estudiante> findByUsuarioNombreContainingIgnoreCaseOrCodigoEstudianteContainingIgnoreCase(String nombre, String codigo);
    List<Estudiante> findByPrograma(String programa);
    List<Estudiante> findBySemestre(Integer semestre);
}