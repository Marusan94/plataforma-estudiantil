package com.edu.plataforma.repository;

import com.edu.plataforma.model.CategoriaNecesidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoriaNecesidadRepository extends JpaRepository<CategoriaNecesidad, Long> {
    Optional<CategoriaNecesidad> findByNombre(String nombre);
}