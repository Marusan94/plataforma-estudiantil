package com.edu.plataforma.dto;

public class AsistenciaResumenDTO {
    private String mes;
    private Long totalPresentes;
    private Long totalAusentes;
    private Long totalJustificados;
    private Double porcentajeAsistencia;

    public AsistenciaResumenDTO() {}

    public AsistenciaResumenDTO(String mes, Long totalPresentes, Long totalAusentes, Long totalJustificados, Double porcentajeAsistencia) {
        this.mes = mes;
        this.totalPresentes = totalPresentes;
        this.totalAusentes = totalAusentes;
        this.totalJustificados = totalJustificados;
        this.porcentajeAsistencia = porcentajeAsistencia;
    }

    public String getMes() { return mes; }
    public void setMes(String mes) { this.mes = mes; }
    public Long getTotalPresentes() { return totalPresentes; }
    public void setTotalPresentes(Long totalPresentes) { this.totalPresentes = totalPresentes; }
    public Long getTotalAusentes() { return totalAusentes; }
    public void setTotalAusentes(Long totalAusentes) { this.totalAusentes = totalAusentes; }
    public Long getTotalJustificados() { return totalJustificados; }
    public void setTotalJustificados(Long totalJustificados) { this.totalJustificados = totalJustificados; }
    public Double getPorcentajeAsistencia() { return porcentajeAsistencia; }
    public void setPorcentajeAsistencia(Double porcentajeAsistencia) { this.porcentajeAsistencia = porcentajeAsistencia; }
}