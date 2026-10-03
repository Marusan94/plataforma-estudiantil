package com.edu.plataforma.dto;

import java.util.ArrayList;
import java.util.List;

public class PerfilEstudianteDTO {
    private Long id;
    private Long estudianteId;
    private String nombreEstudiante;
    private String correoEstudiante;
    private String programa;
    private Integer semestre;
    private String resumen;
    private String intereses;
    private String experiencia;
    private String fotoUrl;
    private List<HabilidadDTO> habilidades = new ArrayList<>();
    private List<ProyectoDTO> proyectos = new ArrayList<>();
    private List<CertificadoDTO> certificados = new ArrayList<>();

    public PerfilEstudianteDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public String getCorreoEstudiante() { return correoEstudiante; }
    public void setCorreoEstudiante(String correoEstudiante) { this.correoEstudiante = correoEstudiante; }
    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getIntereses() { return intereses; }
    public void setIntereses(String intereses) { this.intereses = intereses; }
    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public List<HabilidadDTO> getHabilidades() { return habilidades; }
    public void setHabilidades(List<HabilidadDTO> habilidades) { this.habilidades = habilidades; }
    public List<ProyectoDTO> getProyectos() { return proyectos; }
    public void setProyectos(List<ProyectoDTO> proyectos) { this.proyectos = proyectos; }
    public List<CertificadoDTO> getCertificados() { return certificados; }
    public void setCertificados(List<CertificadoDTO> certificados) { this.certificados = certificados; }
}