package com.edu.plataforma.model;

import jakarta.persistence.*;

@Entity
@Table(name = "empresarios")
public class Empresario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(length = 120)
    private String nombreEmpresa;

    @Column(length = 80)
    private String sector; // Tecnologia, Servicios, Salud, Industria

    @Column(length = 80)
    private String departamento;

    public Empresario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}