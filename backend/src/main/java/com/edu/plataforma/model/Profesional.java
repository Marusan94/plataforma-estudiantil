package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "profesionales_bienestar")
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del profesional es obligatorio")
    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 100)
    private String area; // Psicologia, Trabajo Social, Acompanamiento Academico

    @Column(length = 100)
    private String cargo;

    @Email
    @Column(length = 100)
    private String correo;

    public Profesional() {}

    public Profesional(String nombre, String area, String cargo, String correo) {
        this.nombre = nombre;
        this.area = area;
        this.cargo = cargo;
        this.correo = correo;
    }

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