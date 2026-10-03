package com.edu.plataforma.repository;

import com.edu.plataforma.model.NecesidadApoyo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NecesidadApoyoRepository extends JpaRepository<NecesidadApoyo, Long> {
    List<NecesidadApoyo> findByEstudianteIdOrderByFechaDesc(Long estudianteId);
    List<NecesidadApoyo> findByProfesionalIdOrderByFechaDesc(Long profesionalId);
    List<NecesidadApoyo> findByCategoriaId(Long categoriaId);
    List<NecesidadApoyo> findByEstado(String estado);
    Long countByEstado(String estado);

    @Query("SELECT n.tipo, COUNT(n) FROM NecesidadApoyo n GROUP BY n.tipo")
    List<Object[]> contarPorTipo();

    boolean existsByProfesionalId(Long profesionalId);
}