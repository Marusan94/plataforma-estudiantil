package com.edu.plataforma.model;

import jakarta.persistence.*;

@Entity
@Table(name = "docentes")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(length = 100)
    private String especialidad;

    @Column(length = 50)
    private String nivelAcademico; // Profesional, Especialista, Magister, Doctor

    @Column(length = 100)
    private String departamento; // Ingenieria, Diseno, Salud, Administracion

    public Docente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getNivelAcademico() { return nivelAcademico; }
    public void setNivelAcademico(String nivelAcademico) { this.nivelAcademico = nivelAcademico; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}