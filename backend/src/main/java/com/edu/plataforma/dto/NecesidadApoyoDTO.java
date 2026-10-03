package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NecesidadApoyoDTO {
    private Long id;
    private Long estudianteId;
    private String nombreEstudiante;
    private String programaEstudiante;

    @NotBlank(message = "El tipo de apoyo es obligatorio")
    private String tipo;

    @NotBlank(message = "La descripcion es requerida")
    private String descripcion;

    private LocalDate fecha;
    private String prioridad = "MEDIA";
    private String estado = "PENDIENTE";

    private Long categoriaId;
    private String nombreCategoria;

    private Long profesionalId;
    private String nombreProfesional;

    private List<SeguimientoDTO> seguimientos = new ArrayList<>();

    public NecesidadApoyoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public String getProgramaEstudiante() { return programaEstudiante; }
    public void setProgramaEstudiante(String programaEstudiante) { this.programaEstudiante = programaEstudiante; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }
    public Long getProfesionalId() { return profesionalId; }
    public void setProfesionalId(Long profesionalId) { this.profesionalId = profesionalId; }
    public String getNombreProfesional() { return nombreProfesional; }
    public void setNombreProfesional(String nombreProfesional) { this.nombreProfesional = nombreProfesional; }
    public List<SeguimientoDTO> getSeguimientos() { return seguimientos; }
    public void setSeguimientos(List<SeguimientoDTO> seguimientos) { this.seguimientos = seguimientos; }
}