package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categorias_necesidad")
public class CategoriaNecesidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre; // Emocional, Economica, Academica, Salud

    public CategoriaNecesidad() {}

    public CategoriaNecesidad(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}