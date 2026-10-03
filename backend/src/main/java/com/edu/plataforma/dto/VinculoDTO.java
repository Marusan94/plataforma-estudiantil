package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotNull;

public class VinculoDTO {
    private Long id;

    @NotNull(message = "idFamiliar es obligatorio")
    private Long idFamiliar;
    private String nombreFamiliar;

    @NotNull(message = "idEstudiante es obligatorio")
    private Long idEstudiante;
    private String nombreEstudiante;
    private String programa;
    private Integer semestre;
    private Boolean autorizado = true;

    public VinculoDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdFamiliar() { return idFamiliar; }
    public void setIdFamiliar(Long idFamiliar) { this.idFamiliar = idFamiliar; }
    public String getNombreFamiliar() { return nombreFamiliar; }
    public void setNombreFamiliar(String nombreFamiliar) { this.nombreFamiliar = nombreFamiliar; }
    public Long getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Long idEstudiante) { this.idEstudiante = idEstudiante; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public Boolean getAutorizado() { return autorizado; }
    public void setAutorizado(Boolean autorizado) { this.autorizado = autorizado; }
}