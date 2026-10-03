package com.edu.plataforma.repository;

import com.edu.plataforma.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByEstudianteIdOrderByFechaDesc(Long estudianteId);
    List<Nota> findByMateriaIdAndGrupoId(Long materiaId, Long grupoId);
    List<Nota> findByMateriaDocenteId(Long docenteId);
    List<Nota> findByMateriaDocenteIdAndMateriaId(Long docenteId, Long materiaId);
    List<Nota> findByValorLessThan(Double valor);

    @Query("SELECT AVG(n.valor) FROM Nota n WHERE n.estudiante.id = :estudianteId")
    Double calcularPromedioEstudiante(@Param("estudianteId") Long estudianteId);

    @Query("SELECT n.estudiante.id, AVG(n.valor) FROM Nota n WHERE n.grupo.id = :grupoId GROUP BY n.estudiante.id ORDER BY AVG(n.valor) DESC")
    List<Object[]> rankingPorGrupo(@Param("grupoId") Long grupoId);

    @Query("SELECT AVG(n.valor) FROM Nota n")
    Double calcularPromedioGlobal();
}