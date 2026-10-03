package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoriaNecesidadDTO {
    private Long id;

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    private String nombre;

    public CategoriaNecesidadDTO() {}

    public CategoriaNecesidadDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}