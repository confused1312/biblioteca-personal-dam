package com.biblioteca.model;

import java.time.LocalDate;

public class Prestamo {
    private int idPrestamo;
    private int idLibro;
    private int idAmigo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;
    private String notas;

    public Prestamo() {}

    public Prestamo(int idLibro, int idAmigo, LocalDate fechaPrestamo,
                    LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucionReal, String notas) {
        this.idLibro = idLibro;
        this.idAmigo = idAmigo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.notas = notas;
    }

    public Prestamo(int idPrestamo, int idLibro, int idAmigo, LocalDate fechaPrestamo,
                    LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucionReal, String notas) {
        this.idPrestamo = idPrestamo;
        this.idLibro = idLibro;
        this.idAmigo = idAmigo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.notas = notas;
    }

    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public int getIdAmigo() { return idAmigo; }
    public void setIdAmigo(int idAmigo) { this.idAmigo = idAmigo; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucionPrevista() { return fechaDevolucionPrevista; }
    public void setFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public LocalDate getFechaDevolucionReal() { return fechaDevolucionReal; }
    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public boolean estaDevuelto() {
        return fechaDevolucionReal != null;
    }

    @Override
    public String toString() {
        return "Prestamo " + idPrestamo + " - libro " + idLibro + " a amigo " + idAmigo
                + " (" + fechaPrestamo + ")";
    }
}
