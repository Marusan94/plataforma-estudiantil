package com.edu.plataforma.dto;

import java.util.List;
import java.util.Map;

public class DashboardIndicadoresDTO {
    private long totalUsuarios;
    private long totalEstudiantes;
    private long totalDocentes;
    private long totalFamiliares;
    private double promedioGeneral;
    private double porcentajeAsistencia;
    private long totalSolicitudesBienestar;
    private long solicitudesPendientes;
    private long estudiantesEnRiesgo;
    private List<ResumenMateriaDTO> resumenMaterias;
    private Map<String, Long> distribucionPorGenero;
    private Map<String, Long> solicitudesPorTipo;

    public DashboardIndicadoresDTO() {}

    public long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(long totalUsuarios) { this.totalUsuarios = totalUsuarios; }
    public long getTotalEstudiantes() { return totalEstudiantes; }
    public void setTotalEstudiantes(long totalEstudiantes) { this.totalEstudiantes = totalEstudiantes; }
    public long getTotalDocentes() { return totalDocentes; }
    public void setTotalDocentes(long totalDocentes) { this.totalDocentes = totalDocentes; }
    public long getTotalFamiliares() { return totalFamiliares; }
    public void setTotalFamiliares(long totalFamiliares) { this.totalFamiliares = totalFamiliares; }
    public double getPromedioGeneral() { return promedioGeneral; }
    public void setPromedioGeneral(double promedioGeneral) { this.promedioGeneral = promedioGeneral; }
    public double getPorcentajeAsistencia() { return porcentajeAsistencia; }
    public void setPorcentajeAsistencia(double porcentajeAsistencia) { this.porcentajeAsistencia = porcentajeAsistencia; }
    public long getTotalSolicitudesBienestar() { return totalSolicitudesBienestar; }
    public void setTotalSolicitudesBienestar(long totalSolicitudesBienestar) { this.totalSolicitudesBienestar = totalSolicitudesBienestar; }
    public long getSolicitudesPendientes() { return solicitudesPendientes; }
    public void setSolicitudesPendientes(long solicitudesPendientes) { this.solicitudesPendientes = solicitudesPendientes; }
    public long getEstudiantesEnRiesgo() { return estudiantesEnRiesgo; }
    public void setEstudiantesEnRiesgo(long estudiantesEnRiesgo) { this.estudiantesEnRiesgo = estudiantesEnRiesgo; }
    public List<ResumenMateriaDTO> getResumenMaterias() { return resumenMaterias; }
    public void setResumenMaterias(List<ResumenMateriaDTO> resumenMaterias) { this.resumenMaterias = resumenMaterias; }
    public Map<String, Long> getDistribucionPorGenero() { return distribucionPorGenero; }
    public void setDistribucionPorGenero(Map<String, Long> distribucionPorGenero) { this.distribucionPorGenero = distribucionPorGenero; }
    public Map<String, Long> getSolicitudesPorTipo() { return solicitudesPorTipo; }
    public void setSolicitudesPorTipo(Map<String, Long> solicitudesPorTipo) { this.solicitudesPorTipo = solicitudesPorTipo; }
}