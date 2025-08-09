package com.midinero.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioResponseDTO {
    private Long id;
    private String nombreCompleto;
    private String email;
    private String carrera;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaRegistro;

    // Constructors
    public UsuarioResponseDTO() {}

    public UsuarioResponseDTO(Long id, String nombreCompleto, String email, String carrera, 
                             LocalDate fechaNacimiento, LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.carrera = carrera;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
