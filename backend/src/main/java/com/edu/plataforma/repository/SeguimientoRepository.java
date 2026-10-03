package com.edu.plataforma.repository;

import com.edu.plataforma.model.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {
    List<Seguimiento> findByNecesidadIdOrderByFechaDesc(Long necesidadId);
}