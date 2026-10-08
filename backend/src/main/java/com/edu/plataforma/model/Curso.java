package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @NotBlank(message = "El codigo es obligatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(length = 100)
    private String categoria;

    @Column(length = 50)
    private String nivel;

    private Integer horas;

    @Column(nullable = false)
    private Boolean activo = true;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Actividad> actividades = new ArrayList<>();

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Evaluacion> evaluaciones = new ArrayList<>();

    public Curso() {}

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
    public List<Actividad> getActividades() { return actividades; }
    public void setActividades(List<Actividad> actividades) { this.actividades = actividades; }
    public List<Evaluacion> getEvaluaciones() { return evaluaciones; }
    public void setEvaluaciones(List<Evaluacion> evaluaciones) { this.evaluaciones = evaluaciones; }
}
