package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    @JsonIgnore
    private PerfilEstudiante perfil;

    @NotBlank(message = "El titulo es obligatorio")
    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 255)
    private String url;

    @Column(length = 150)
    private String tecnologias;

    public Proyecto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PerfilEstudiante getPerfil() { return perfil; }
    public void setPerfil(PerfilEstudiante perfil) { this.perfil = perfil; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getTecnologias() { return tecnologias; }
    public void setTecnologias(String tecnologias) { this.tecnologias = tecnologias; }
}