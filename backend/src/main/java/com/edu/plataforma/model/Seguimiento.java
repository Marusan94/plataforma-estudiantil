package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "seguimientos_apoyo")
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "necesidad_id", nullable = false)
    @JsonIgnore
    private NecesidadApoyo necesidad;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 30)
    private String estado; // EN_ATENCION, CERRADA, SEGUIMIENTO_CONTINUO

    public Seguimiento() {}

    public Seguimiento(NecesidadApoyo necesidad, LocalDate fecha, String descripcion, String estado) {
        this.necesidad = necesidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NecesidadApoyo getNecesidad() { return necesidad; }
    public void setNecesidad(NecesidadApoyo necesidad) { this.necesidad = necesidad; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}