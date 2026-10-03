package com.edu.plataforma.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "certificados")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    @JsonIgnore
    private PerfilEstudiante perfil;

    @NotBlank(message = "El nombre del certificado es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 150)
    private String institucion;

    private LocalDate fecha;

    @Column(length = 255)
    private String urlArchivo;

    public Certificado() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PerfilEstudiante getPerfil() { return perfil; }
    public void setPerfil(PerfilEstudiante perfil) { this.perfil = perfil; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
}