package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "notas")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El valor de la nota es obligatorio")
    @DecimalMin(value = "0.0", message = "La nota minima es 0.0")
    @DecimalMax(value = "5.0", message = "La nota maxima es 5.0")
    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false, length = 50)
    private String tipoEvaluacion; // Parcial, Quiz, Taller, Proyecto, Seguimiento

    private LocalDate fecha = LocalDate.now();

    @Column(length = 255)
    private String comentario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    public Nota() {}

    public Nota(Double valor, String tipoEvaluacion, LocalDate fecha, String comentario, Estudiante estudiante, Materia materia, Grupo grupo) {
        this.valor = valor;
        this.tipoEvaluacion = tipoEvaluacion;
        this.fecha = fecha;
        this.comentario = comentario;
        this.estudiante = estudiante;
        this.materia = materia;
        this.grupo = grupo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getTipoEvaluacion() { return tipoEvaluacion; }
    public void setTipoEvaluacion(String tipoEvaluacion) { this.tipoEvaluacion = tipoEvaluacion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
}