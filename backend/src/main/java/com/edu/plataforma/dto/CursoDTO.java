package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CursoDTO {
    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    private String descripcion;

    @NotBlank(message = "El codigo es obligatorio")
    private String codigo;

    private String categoria;
    private String nivel;
    private Integer horas;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    public CursoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public Integer getHoras() { return horas; }
    public void setHoras(Integer horas) { this.horas = horas; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
