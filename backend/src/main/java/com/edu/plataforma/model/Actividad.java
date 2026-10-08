package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    @JsonIgnore
    private Curso curso;

    @NotBlank(message = "El titulo es obligatorio")
    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @Column(length = 50)
    private String tipo;

    @Column(length = 4000)
    private String contenido;

    @Column(nullable = false)
    private Integer orden = 0;

    @Column(nullable = false)
    private Integer puntos = 10;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public Actividad() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
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
