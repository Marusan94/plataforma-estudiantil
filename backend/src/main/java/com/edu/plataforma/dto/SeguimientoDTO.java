package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class SeguimientoDTO {
    private Long id;
    private Long necesidadId;
    private LocalDate fecha;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String descripcion;

    @NotBlank(message = "El estado no puede estar vacio")
    private String estado;

    public SeguimientoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getNecesidadId() { return necesidadId; }
    public void setNecesidadId(Long necesidadId) { this.necesidadId = necesidadId; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}