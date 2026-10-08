package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "recursos_biblioteca")
public class RecursoBiblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @Column(length = 150)
    private String autor;

    @Column(length = 50)
    private String tipo;

    @Column(length = 100)
    private String categoria;

    @Column(nullable = true, length = 500)
    private String urlArchivo;

    @Column(nullable = true)
    private Double tamanioMb;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public RecursoBiblioteca() {}

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
