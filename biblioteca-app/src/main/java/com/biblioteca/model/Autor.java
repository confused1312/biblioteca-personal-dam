package com.biblioteca.model;

public class Autor {
    private int idAutor;
    private String nombre;
    private String apellidos;
    private String nacionalidad;
    private Integer anioNacimiento;

    public Autor() {}

    public Autor(String nombre, String apellidos, String nacionalidad, Integer anioNacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nacionalidad = nacionalidad;
        this.anioNacimiento = anioNacimiento;
    }

    public Autor(int idAutor, String nombre, String apellidos, String nacionalidad, Integer anioNacimiento) {
        this.idAutor = idAutor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nacionalidad = nacionalidad;
        this.anioNacimiento = anioNacimiento;
    }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public Integer getAnioNacimiento() { return anioNacimiento; }
    public void setAnioNacimiento(Integer anioNacimiento) { this.anioNacimiento = anioNacimiento; }

    @Override
    public String toString() {
        return idAutor + " - " + nombre + " " + apellidos + " (" + nacionalidad + ")";
    }
}
