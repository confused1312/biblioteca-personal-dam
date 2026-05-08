package com.biblioteca.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Validador {

    private static final Pattern PATRON_EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PATRON_ISBN =
            Pattern.compile("^[0-9\\-]{10,20}$");

    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^[+]?[0-9 ]{6,20}$");

    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.isBlank();
    }

    public static boolean esEmailValido(String email) {
        return email != null && PATRON_EMAIL.matcher(email).matches();
    }

    public static boolean esIsbnValido(String isbn) {
        return isbn != null && PATRON_ISBN.matcher(isbn).matches();
    }

    public static boolean esAnioValido(Integer anio) {
        if (anio == null) return false;
        int actual = LocalDate.now().getYear();
        return anio >= 1000 && anio <= actual + 1;
    }

    public static boolean esTelefonoValido(String tel) {
        return tel != null && PATRON_TELEFONO.matcher(tel).matches();
    }

    public static boolean esEnteroPositivo(Integer n) {
        return n != null && n > 0;
    }
}