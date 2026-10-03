package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "necesidades_apoyo")
public class NecesidadApoyo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El tipo de apoyo es obligatorio")
    @Column(nullable = false, length = 80)
    private String tipo; // Psicologico, Academico, Economico, Orientacion

    @NotBlank(message = "La descripcion es requerida")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    @Column(nullable = false, length = 30)
    private String prioridad = "MEDIA"; // ALTA, MEDIA, BAJA

    @Column(nullable = false, length = 30)
    private String estado = "PENDIENTE"; // PENDIENTE, EN_ATENCION, CERRADA

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private CategoriaNecesidad categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profesional_id")
    private Profesional profesional;

    @OneToMany(mappedBy = "necesidad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("necesidad")
    private List<Seguimiento> seguimientos = new ArrayList<>();

    public NecesidadApoyo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public CategoriaNecesidad getCategoria() { return categoria; }
    public void setCategoria(CategoriaNecesidad categoria) { this.categoria = categoria; }
    public Profesional getProfesional() { return profesional; }
    public void setProfesional(Profesional profesional) { this.profesional = profesional; }
    public List<Seguimiento> getSeguimientos() { return seguimientos; }
    public void setSeguimientos(List<Seguimiento> seguimientos) { this.seguimientos = seguimientos; }
}