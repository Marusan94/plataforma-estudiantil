package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones_familiares")
public class NotificacionFamiliar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familiar_id", nullable = false)
    private Familiar familiar;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String mensaje;

    private LocalDateTime fechaEnvio = LocalDateTime.now();

    @Column(nullable = false, length = 20)
    private String estado = "NO_LEIDA"; // LEIDA, NO_LEIDA

    public NotificacionFamiliar() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Familiar getFamiliar() { return familiar; }
    public void setFamiliar(Familiar familiar) { this.familiar = familiar; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}