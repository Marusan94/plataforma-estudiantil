package com.edu.plataforma.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class NotaDTO {
    private Long id;

    @NotNull(message = "estudianteId es obligatorio")
    private Long estudianteId;
    private String nombreEstudiante;

    @NotNull(message = "materiaId es obligatorio")
    private Long materiaId;
    private String nombreMateria;

    private Long grupoId;
    private String nombreGrupo;

    @NotNull(message = "El valor de la nota es obligatorio")
    @DecimalMin(value = "0.0", message = "La nota minima es 0.0")
    @DecimalMax(value = "5.0", message = "La nota maxima es 5.0")
    private Double valor;

    private String tipoEvaluacion; // Parcial, Quiz, Proyecto, etc.
    private LocalDate fecha;
    private String comentario;
    private String recomendacion; // Generada si nota < 3.0

    public NotaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public Long getMateriaId() { return materiaId; }
    public void setMateriaId(Long materiaId) { this.materiaId = materiaId; }
    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }
    public Long getGrupoId() { return grupoId; }
    public void setGrupoId(Long grupoId) { this.grupoId = grupoId; }
    public String getNombreGrupo() { return nombreGrupo; }
    public void setNombreGrupo(String nombreGrupo) { this.nombreGrupo = nombreGrupo; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getTipoEvaluacion() { return tipoEvaluacion; }
    public void setTipoEvaluacion(String tipoEvaluacion) { this.tipoEvaluacion = tipoEvaluacion; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public String getRecomendacion() { return recomendacion; }
    public void setRecomendacion(String recomendacion) { this.recomendacion = recomendacion; }
}