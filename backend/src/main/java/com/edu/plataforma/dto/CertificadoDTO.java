package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CertificadoDTO {
    private Long id;
    private Long perfilId;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String institucion;
    private LocalDate fecha;
    private String urlArchivo;

    public CertificadoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPerfilId() { return perfilId; }
    public void setPerfilId(Long perfilId) { this.perfilId = perfilId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
}