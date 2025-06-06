package com.centroestetico.empleados;

public class Empleado {
    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private int porcentajeGanancia;

    public Empleado() {
    }

    public Empleado(int id, String nombre, String apellido, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.porcentajeGanancia = 50; // Por defecto si no lo asignan
    }

    // Constructor completo
    public Empleado(int id, String nombre, String apellido, String telefono, String email, int porcentajeGanancia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.porcentajeGanancia = porcentajeGanancia;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPorcentajeGanancia() {
        return porcentajeGanancia;
    }

    public void setPorcentajeGanancia(int porcentajeGanancia) {
        this.porcentajeGanancia = porcentajeGanancia;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}

