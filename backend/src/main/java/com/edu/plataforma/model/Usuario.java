package com.edu.plataforma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El correo no puede estar vacio")
    @Email(message = "El correo electronico debe ser valido")
    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @NotBlank(message = "La contrasena no puede estar vacia")
    @Size(min = 6, message = "La contrasena debe tener minimo 6 caracteres")
    @Column(nullable = false)
    private String contrasena;

    @NotBlank(message = "El rol es obligatorio")
    @Column(nullable = false, length = 30)
    private String rol; // ESTUDIANTE, DOCENTE, FAMILIAR, BIENESTAR, ADMIN, EMPRESARIO

    @Column(nullable = false, length = 20)
    private String estado = "ACTIVO"; // ACTIVO, INACTIVO

    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public Usuario() {}

    public Usuario(Long id, String nombre, String correo, String contrasena, String rol, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.estado = estado;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}