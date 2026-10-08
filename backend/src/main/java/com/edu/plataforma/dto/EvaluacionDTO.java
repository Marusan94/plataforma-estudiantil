package com.edu.plataforma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EvaluacionDTO {
    private Long id;

    @NotNull(message = "cursoId es obligatorio")
    private Long cursoId;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    private String descripcion;
    private String tipoEvaluacion;
    private Integer puntajeMaximo;
    private String preguntasJson;
    private LocalDateTime fechaCreacion;

    public EvaluacionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipoEvaluacion() { return tipoEvaluacion; }
    public void setTipoEvaluacion(String tipoEvaluacion) { this.tipoEvaluacion = tipoEvaluacion; }
    public Integer getPuntajeMaximo() { return puntajeMaximo; }
    public void setPuntajeMaximo(Integer puntajeMaximo) { this.puntajeMaximo = puntajeMaximo; }
    public String getPreguntasJson() { return preguntasJson; }
    public void setPreguntasJson(String preguntasJson) { this.preguntasJson = preguntasJson; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
