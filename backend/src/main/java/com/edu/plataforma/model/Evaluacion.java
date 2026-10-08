package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluaciones")
public class Evaluacion {

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
    private String tipoEvaluacion = "Quiz";

    @Column(nullable = false)
    private Integer puntajeMaximo = 100;

    @Column(length = 8000, nullable = true)
    private String preguntasJson;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public Evaluacion() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipoEvaluacion() { return tipoEvaluacion; }
    public void setTipoEvaluacion(String tipoEvaluacion) { this.tipoEvaluacion = tipoEvaluacion; }
    public Integer getPuntajeMaximo() { return puntajeMaximo; }
    public void setPuntajeMaximo(Integer puntajeMaximo) { this.puntajeMaximo = puntajeMaximo; }
    public String getPreguntasJson() { return preguntasJson; }
    public void setPreguntasJson(String preguntasJson) { this.preguntasJson = preguntasJson; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
