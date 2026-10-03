package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;

public class TipoApoyoDTO {
    private Long id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    private String descripcion;

    public TipoApoyoDTO() {}

    public TipoApoyoDTO(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}