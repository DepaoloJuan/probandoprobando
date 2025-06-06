package com.centroestetico.turnos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private int id;
    private int idCliente;
    private int idServicio;
    private int idEmpleado;
    private LocalDate fecha;
    private LocalTime hora;
    private double costo;
    private String estado;

    // Constructor vacío (por si después queremos usar JavaFX o frameworks que lo necesiten)
    public Turno() {
    }

    // Constructor completo
    public Turno(int id, int idCliente, int idServicio, int idEmpleado, LocalDate fecha, LocalTime hora, double costo, String estado) {
        this.id = id;
        this.idCliente = idCliente;
        this.idServicio = idServicio;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.hora = hora;
        this.costo = costo;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
