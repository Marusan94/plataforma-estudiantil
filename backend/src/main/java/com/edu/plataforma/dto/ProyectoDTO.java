package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;

public class ProyectoDTO {
    private Long id;
    private Long perfilId;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    private String descripcion;
    private String url;
    private String tecnologias;

    public ProyectoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPerfilId() { return perfilId; }
    public void setPerfilId(Long perfilId) { this.perfilId = perfilId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getTecnologias() { return tecnologias; }
    public void setTecnologias(String tecnologias) { this.tecnologias = tecnologias; }
}