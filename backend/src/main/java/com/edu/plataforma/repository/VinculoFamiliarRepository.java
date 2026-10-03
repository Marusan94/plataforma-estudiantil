package com.edu.plataforma.repository;

import com.edu.plataforma.model.VinculoFamiliarEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VinculoFamiliarRepository extends JpaRepository<VinculoFamiliarEstudiante, Long> {
    List<VinculoFamiliarEstudiante> findByFamiliarId(Long familiarId);
    List<VinculoFamiliarEstudiante> findByEstudianteId(Long estudianteId);
    Optional<VinculoFamiliarEstudiante> findByFamiliarIdAndEstudianteId(Long familiarId, Long estudianteId);
}