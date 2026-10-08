package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_cursos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"estudiante_id", "curso_id"}))
public class ProgresoCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "estudianteId es obligatorio")
    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @NotNull(message = "cursoId es obligatorio")
    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    @Column(nullable = false)
    private Integer porcentaje = 0;

    @Column(nullable = false, length = 30)
    private String estado = "NO_INICIADO";

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    public ProgresoCurso() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
    public Integer getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Integer porcentaje) { this.porcentaje = porcentaje; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
