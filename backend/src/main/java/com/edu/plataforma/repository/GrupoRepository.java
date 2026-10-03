package com.edu.plataforma.repository;

import com.edu.plataforma.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByMateriaId(Long materiaId);
    List<Grupo> findByMateriaDocenteId(Long docenteId);
}