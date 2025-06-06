package com.centroestetico.servicios;

import java.time.LocalDate;

public class Servicio {

    private int id;
    private int idCliente;
    private int idEmpleado;
    private LocalDate fecha;
    private String descripcion;
    private double monto;
    private String estado;
    private String nombreEmpleado;



    public Servicio() {
        // Constructor por defecto necesario para uso flexible
    }



    public Servicio(int id, int idCliente, int idEmpleado, LocalDate fecha, String descripcion, double monto, String estado) {
        this.id = id;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.monto = monto;
        this.estado = estado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }


    public int getIdCliente() {
        return idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
