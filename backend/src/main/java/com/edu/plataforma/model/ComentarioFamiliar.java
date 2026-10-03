package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios_familiares")
public class ComentarioFamiliar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familiar_id", nullable = false)
    private Familiar familiar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String comentario;

    private LocalDateTime fecha = LocalDateTime.now();

    public ComentarioFamiliar() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Familiar getFamiliar() { return familiar; }
    public void setFamiliar(Familiar familiar) { this.familiar = familiar; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}