package com.edu.plataforma.repository;

import com.edu.plataforma.model.ComentarioFamiliar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComentarioFamiliarRepository extends JpaRepository<ComentarioFamiliar, Long> {
    List<ComentarioFamiliar> findByEstudianteId(Long estudianteId);
    List<ComentarioFamiliar> findByFamiliarId(Long familiarId);
}