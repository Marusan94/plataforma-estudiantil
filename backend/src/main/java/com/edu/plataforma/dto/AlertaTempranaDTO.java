package com.edu.plataforma.dto;

public class AlertaTempranaDTO {
    private Long estudianteId;
    private String nombreEstudiante;
    private String programa;
    private Double promedio;
    private Double porcentajeAsistencia;
    private String nivelRiesgo; // ALTO, MEDIO, BAJO
    private String motivo;

    public AlertaTempranaDTO() {}

    public AlertaTempranaDTO(Long estudianteId, String nombreEstudiante, String programa, Double promedio, Double porcentajeAsistencia, String nivelRiesgo, String motivo) {
        this.estudianteId = estudianteId;
        this.nombreEstudiante = nombreEstudiante;
        this.programa = programa;
        this.promedio = promedio;
        this.porcentajeAsistencia = porcentajeAsistencia;
        this.nivelRiesgo = nivelRiesgo;
        this.motivo = motivo;
    }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }
    public Double getPromedio() { return promedio; }
    public void setPromedio(Double promedio) { this.promedio = promedio; }
    public Double getPorcentajeAsistencia() { return porcentajeAsistencia; }
    public void setPorcentajeAsistencia(Double porcentajeAsistencia) { this.porcentajeAsistencia = porcentajeAsistencia; }
    public String getNivelRiesgo() { return nivelRiesgo; }
    public void setNivelRiesgo(String nivelRiesgo) { this.nivelRiesgo = nivelRiesgo; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}