package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class EstudianteDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String correo;

    @NotBlank(message = "El programa es obligatorio")
    private String programa;

    private Integer semestre = 1;
    private LocalDate fechaNacimiento;
    private String codigoEstudiante;
    private String genero;

    public EstudianteDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getCodigoEstudiante() { return codigoEstudiante; }
    public void setCodigoEstudiante(String codigoEstudiante) { this.codigoEstudiante = codigoEstudiante; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}