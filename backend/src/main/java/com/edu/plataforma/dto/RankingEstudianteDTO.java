package com.edu.plataforma.dto;

public class RankingEstudianteDTO {
    private Long estudianteId;
    private String nombreEstudiante;
    private Long grupoId;
    private String nombreGrupo;
    private Double promedio;
    private Integer posicion;

    public RankingEstudianteDTO() {}

    public RankingEstudianteDTO(Long estudianteId, String nombreEstudiante, Long grupoId, String nombreGrupo, Double promedio, Integer posicion) {
        this.estudianteId = estudianteId;
        this.nombreEstudiante = nombreEstudiante;
        this.grupoId = grupoId;
        this.nombreGrupo = nombreGrupo;
        this.promedio = promedio;
        this.posicion = posicion;
    }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public Long getGrupoId() { return grupoId; }
    public void setGrupoId(Long grupoId) { this.grupoId = grupoId; }
    public String getNombreGrupo() { return nombreGrupo; }
    public void setNombreGrupo(String nombreGrupo) { this.nombreGrupo = nombreGrupo; }
    public Double getPromedio() { return promedio; }
    public void setPromedio(Double promedio) { this.promedio = promedio; }
    public Integer getPosicion() { return posicion; }
    public void setPosicion(Integer posicion) { this.posicion = posicion; }
}