package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class AsistenciaLoteDTO {
    @NotNull(message = "grupoId es obligatorio")
    private Long grupoId;

    @NotNull(message = "fecha es obligatoria")
    private LocalDate fecha;

    @NotEmpty(message = "La lista de asistencias no puede estar vacia")
    private List<AsistenciaItemDTO> asistencias;

    public static class AsistenciaItemDTO {
        private Long estudianteId;
        private String estado; // PRESENTE, AUSENTE, JUSTIFICADO
        private String observaciones;
        private Boolean justificada;

        public AsistenciaItemDTO() {}

        public Long getEstudianteId() { return estudianteId; }
        public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public String getObservaciones() { return observaciones; }
        public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
        public Boolean getJustificada() { return justificada; }
        public void setJustificada(Boolean justificada) { this.justificada = justificada; }
    }

    public AsistenciaLoteDTO() {}

    public Long getGrupoId() { return grupoId; }
    public void setGrupoId(Long grupoId) { this.grupoId = grupoId; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public List<AsistenciaItemDTO> getAsistencias() { return asistencias; }
    public void setAsistencias(List<AsistenciaItemDTO> asistencias) { this.asistencias = asistencias; }
}