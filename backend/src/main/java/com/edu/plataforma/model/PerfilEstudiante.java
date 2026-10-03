package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "perfiles_estudiantes")
public class PerfilEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false, unique = true)
    private Estudiante estudiante;

    @Column(columnDefinition = "TEXT")
    private String resumen;

    @Column(length = 255)
    private String intereses;

    @Column(columnDefinition = "TEXT")
    private String experiencia;

    @Column(length = 255)
    private String fotoUrl;

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("perfil")
    private List<Habilidad> habilidades = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("perfil")
    private List<Proyecto> proyectos = new ArrayList<>();

    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("perfil")
    private List<Certificado> certificados = new ArrayList<>();

    public PerfilEstudiante() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getIntereses() { return intereses; }
    public void setIntereses(String intereses) { this.intereses = intereses; }
    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public List<Habilidad> getHabilidades() { return habilidades; }
    public void setHabilidades(List<Habilidad> habilidades) { this.habilidades = habilidades; }
    public List<Proyecto> getProyectos() { return proyectos; }
    public void setProyectos(List<Proyecto> proyectos) { this.proyectos = proyectos; }
    public List<Certificado> getCertificados() { return certificados; }
    public void setCertificados(List<Certificado> certificados) { this.certificados = certificados; }
}