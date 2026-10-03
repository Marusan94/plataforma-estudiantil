package com.edu.plataforma.repository;

import com.edu.plataforma.model.Habilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HabilidadRepository extends JpaRepository<Habilidad, Long> {
    List<Habilidad> findByPerfilId(Long perfilId);
}