package com.edu.plataforma.dto;

public class NotaPromedioDTO {
    private Long estudianteId;
    private String nombreEstudiante;
    private Double promedio;
    private Integer numeroMaterias;

    public NotaPromedioDTO() {}

    public NotaPromedioDTO(Long estudianteId, String nombreEstudiante, Double promedio, Integer numeroMaterias) {
        this.estudianteId = estudianteId;
        this.nombreEstudiante = nombreEstudiante;
        this.promedio = promedio;
        this.numeroMaterias = numeroMaterias;
    }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public Double getPromedio() { return promedio; }
    public void setPromedio(Double promedio) { this.promedio = promedio; }
    public Integer getNumeroMaterias() { return numeroMaterias; }
    public void setNumeroMaterias(Integer numeroMaterias) { this.numeroMaterias = numeroMaterias; }
}