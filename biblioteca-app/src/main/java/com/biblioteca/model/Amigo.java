package com.biblioteca.model;

public class Amigo {
    private int idAmigo;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;

    public Amigo() {}

    public Amigo(String nombre, String apellidos, String email, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
    }

    public Amigo(int idAmigo, String nombre, String apellidos, String email, String telefono) {
        this.idAmigo = idAmigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
    }

    public int getIdAmigo() { return idAmigo; }
    public void setIdAmigo(int idAmigo) { this.idAmigo = idAmigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return idAmigo + " - " + nombre + " " + apellidos + " (" + email + ")";
    }
}
