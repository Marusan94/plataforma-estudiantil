package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class HabilidadDTO {
    private Long id;
    private Long perfilId;

    @NotBlank(message = "El nombre de la habilidad es requerido")
    @Size(min = 2, max = 100)
    private String nombre;

    private String nivel; // Basico, Intermedio, Avanzado
    private String tipo;  // Tecnica, Blanda

    public HabilidadDTO() {}

    public HabilidadDTO(Long id, Long perfilId, String nombre, String nivel, String tipo) {
        this.id = id;
        this.perfilId = perfilId;
        this.nombre = nombre;
        this.nivel = nivel;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPerfilId() { return perfilId; }
    public void setPerfilId(Long perfilId) { this.perfilId = perfilId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}