package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ProgresoCursoDTO {
    private Long id;

    @NotNull(message = "estudianteId es obligatorio")
    private Long estudianteId;

    @NotNull(message = "cursoId es obligatorio")
    private Long cursoId;

    private Integer porcentaje;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public ProgresoCursoDTO() {}

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
