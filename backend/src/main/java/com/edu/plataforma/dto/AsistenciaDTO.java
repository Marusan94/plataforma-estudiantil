package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AsistenciaDTO {
    private Long id;

    @NotNull(message = "estudianteId es obligatorio")
    private Long estudianteId;
    private String nombreEstudiante;

    @NotNull(message = "grupoId es obligatorio")
    private Long grupoId;
    private String nombreGrupo;

    @NotNull(message = "fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "estado es obligatorio")
    private String estado; // PRESENTE, AUSENTE, JUSTIFICADO

    private String observaciones;
    private Boolean justificada = false;

    public AsistenciaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public Long getGrupoId() { return grupoId; }
    public void setGrupoId(Long grupoId) { this.grupoId = grupoId; }
    public String getNombreGrupo() { return nombreGrupo; }
    public void setNombreGrupo(String nombreGrupo) { this.nombreGrupo = nombreGrupo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Boolean getJustificada() { return justificada; }
    public void setJustificada(Boolean justificada) { this.justificada = justificada; }
}