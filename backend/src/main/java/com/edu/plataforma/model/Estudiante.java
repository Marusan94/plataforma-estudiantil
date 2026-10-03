package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @NotBlank(message = "El programa academico es obligatorio")
    @Column(nullable = false, length = 100)
    private String programa;

    @Column(nullable = false)
    private Integer semestre = 1;

    private LocalDate fechaNacimiento;

    @Column(length = 30, unique = true)
    private String codigoEstudiante;

    @Column(length = 20)
    private String genero = "Otro"; // Masculino, Femenino, Otro

    public Estudiante() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
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