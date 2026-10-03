package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "habilidades")
public class Habilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    @JsonIgnore
    private PerfilEstudiante perfil;

    @NotBlank(message = "El nombre de la habilidad es requerido")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 50)
    private String nivel; // Basico, Intermedio, Avanzado

    @Column(length = 50)
    private String tipo; // Tecnica, Blanda

    public Habilidad() {}

    public Habilidad(String nombre, String nivel, String tipo, PerfilEstudiante perfil) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.tipo = tipo;
        this.perfil = perfil;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PerfilEstudiante getPerfil() { return perfil; }
    public void setPerfil(PerfilEstudiante perfil) { this.perfil = perfil; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}