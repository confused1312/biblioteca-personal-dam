package com.biblioteca.util;

public class ResultadoOperacion {

    private final boolean exito;
    private final String mensaje;

    private ResultadoOperacion(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public static ResultadoOperacion exito(String mensaje) {
        return new ResultadoOperacion(true, mensaje);
    }

    public static ResultadoOperacion error(String mensaje) {
        return new ResultadoOperacion(false, mensaje);
    }

    public boolean esExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }
}