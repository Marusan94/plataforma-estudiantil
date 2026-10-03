package com.edu.plataforma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ProfesionalDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String area;
    private String cargo;

    @Email
    private String correo;

    public ProfesionalDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}