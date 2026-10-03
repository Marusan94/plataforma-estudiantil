package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "asistencias", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"estudiante_id", "grupo_id", "fecha"})
})
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false, length = 30)
    private String estado; // PRESENTE, AUSENTE, JUSTIFICADO

    @Column(length = 255)
    private String observaciones;

    @Column(nullable = false)
    private Boolean justificada = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    public Asistencia() {}

    public Asistencia(LocalDate fecha, String estado, String observaciones, Boolean justificada, Estudiante estudiante, Grupo grupo) {
        this.fecha = fecha;
        this.estado = estado;
        this.observaciones = observaciones;
        this.justificada = justificada != null ? justificada : false;
        this.estudiante = estudiante;
        this.grupo = grupo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Boolean getJustificada() { return justificada; }
    public void setJustificada(Boolean justificada) { this.justificada = justificada; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
}