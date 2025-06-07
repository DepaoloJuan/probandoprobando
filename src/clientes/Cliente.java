package com.centroestetico.clientes;

public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;

    public Cliente(int id, String nombre, String apellido, String telefono, String email) {
        this.id       = id;
        this.nombre   = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email    = email;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }

    // Getters y setters...
    public int    getId()        { return id; }
    public void   setId(int id)  { this.id = id; }
    public String getNombre()    { return nombre; }
    public void   setNombre(String n) { this.nombre = n; }
    public String getApellido()  { return apellido; }
    public void   setApellido(String a) { this.apellido = a; }
    public String getTelefono()  { return telefono; }
    public void   setTelefono(String t) { this.telefono = t; }
    public String getEmail()     { return email; }
    public void   setEmail(String e) { this.email = e; }
}

