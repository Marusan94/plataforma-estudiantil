package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;

public class DocenteDTO {
    private Long id;
    private Long usuarioId;
    private String nombre;
    private String correo;
    private String especialidad;
    private String nivelAcademico;
    private String departamento;

    public DocenteDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getNivelAcademico() { return nivelAcademico; }
    public void setNivelAcademico(String nivelAcademico) { this.nivelAcademico = nivelAcademico; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}