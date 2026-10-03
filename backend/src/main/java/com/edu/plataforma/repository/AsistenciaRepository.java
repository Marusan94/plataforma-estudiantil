package com.edu.plataforma.repository;

import com.edu.plataforma.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByEstudianteIdOrderByFechaDesc(Long estudianteId);
    List<Asistencia> findByGrupoIdAndFechaOrderByEstudianteUsuarioNombreAsc(Long grupoId, LocalDate fecha);
    List<Asistencia> findByGrupoIdOrderByFechaDesc(Long grupoId);
    List<Asistencia> findByFecha(LocalDate fecha);
    List<Asistencia> findByFechaBetween(LocalDate inicio, LocalDate fin);
    Optional<Asistencia> findByEstudianteIdAndGrupoIdAndFecha(Long estudianteId, Long grupoId, LocalDate fecha);

    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.estudiante.id = :estudianteId")
    Long contarTotalSesionesPorEstudiante(@Param("estudianteId") Long estudianteId);

    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.estudiante.id = :estudianteId AND a.estado = 'PRESENTE'")
    Long contarPresentesPorEstudiante(@Param("estudianteId") Long estudianteId);

    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.estudiante.id = :estudianteId AND a.estado = 'AUSENTE'")
    Long contarAusenciasPorEstudiante(@Param("estudianteId") Long estudianteId);

    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.estado = 'PRESENTE'")
    Long contarTotalPresentes();

    @Query("SELECT COUNT(a) FROM Asistencia a")
    Long contarTotalRegistros();
}