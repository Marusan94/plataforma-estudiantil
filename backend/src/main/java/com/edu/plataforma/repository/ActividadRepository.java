package com.edu.plataforma.repository;

import com.edu.plataforma.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
    List<Actividad> findByCursoId(Long cursoId);
}
