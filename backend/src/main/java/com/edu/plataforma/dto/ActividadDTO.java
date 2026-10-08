package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ActividadDTO {
    private Long id;

    @NotNull(message = "cursoId es obligatorio")
    private Long cursoId;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    private String descripcion;
    private String tipo;
    private String contenido;
    private Integer orden;
    private Integer puntos;
    private LocalDateTime fechaCreacion;

    public ActividadDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public Integer getPuntos() { return puntos; }
    public void setPuntos(Integer puntos) { this.puntos = puntos; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
