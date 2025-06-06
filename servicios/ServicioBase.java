package com.centroestetico.servicios;

public class ServicioBase {
    private int id;
    private String categoria;
    private String descripcion;
    private double precio;

    // Constructor vacío
    public ServicioBase() {
    }

    // Constructor completo
    public ServicioBase(int id, String categoria, String descripcion, double precio) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    // Para mostrar nombre del servicio en ComboBox
    @Override
    public String toString() {
        return descripcion;
    }
}
