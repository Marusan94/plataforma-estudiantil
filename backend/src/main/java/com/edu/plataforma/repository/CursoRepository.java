package com.edu.plataforma.repository;

import com.edu.plataforma.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByCodigo(String codigo);
}
