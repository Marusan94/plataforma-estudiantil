package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "materias")
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank(message = "El codigo de la materia es obligatorio")
    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docente_id")
    private Docente docente;

    public Materia() {}

    public Materia(String nombre, String codigo, Docente docente) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.docente = docente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public Docente getDocente() { return docente; }
    public void setDocente(Docente docente) { this.docente = docente; }
}