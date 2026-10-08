package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RecursoBibliotecaDTO {
    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    private String descripcion;
    private String autor;
    private String tipo;
    private String categoria;
    private String urlArchivo;
    private Double tamanioMb;
    private LocalDateTime fechaCreacion;

    public RecursoBibliotecaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
    public Double getTamanioMb() { return tamanioMb; }
    public void setTamanioMb(Double tamanioMb) { this.tamanioMb = tamanioMb; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
