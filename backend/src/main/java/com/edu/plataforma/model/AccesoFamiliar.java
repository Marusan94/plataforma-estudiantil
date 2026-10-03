package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "accesos_familiares")
public class AccesoFamiliar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familiar_id", nullable = false)
    private Familiar familiar;

    @NotNull(message = "La fecha de acceso es requerida")
    @Column(nullable = false)
    private LocalDateTime fechaAcceso = LocalDateTime.now();

    @NotBlank(message = "La accion realizada no puede estar vacia")
    @Column(nullable = false, length = 150)
    private String accionRealizada;

    public AccesoFamiliar() {}

    public AccesoFamiliar(Familiar familiar, String accionRealizada) {
        this.familiar = familiar;
        this.fechaAcceso = LocalDateTime.now();
        this.accionRealizada = accionRealizada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Familiar getFamiliar() { return familiar; }
    public void setFamiliar(Familiar familiar) { this.familiar = familiar; }
    public LocalDateTime getFechaAcceso() { return fechaAcceso; }
    public void setFechaAcceso(LocalDateTime fechaAcceso) { this.fechaAcceso = fechaAcceso; }
    public String getAccionRealizada() { return accionRealizada; }
    public void setAccionRealizada(String accionRealizada) { this.accionRealizada = accionRealizada; }
}