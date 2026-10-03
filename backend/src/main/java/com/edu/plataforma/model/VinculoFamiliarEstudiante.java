package com.edu.plataforma.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vinculos_familiar_estudiante", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"familiar_id", "estudiante_id"})
})
public class VinculoFamiliarEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "familiar_id", nullable = false)
    private Familiar familiar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(nullable = false)
    private Boolean autorizado = true;

    public VinculoFamiliarEstudiante() {}

    public VinculoFamiliarEstudiante(Familiar familiar, Estudiante estudiante, Boolean autorizado) {
        this.familiar = familiar;
        this.estudiante = estudiante;
        this.autorizado = autorizado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Familiar getFamiliar() { return familiar; }
    public void setFamiliar(Familiar familiar) { this.familiar = familiar; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public Boolean getAutorizado() { return autorizado; }
    public void setAutorizado(Boolean autorizado) { this.autorizado = autorizado; }
}