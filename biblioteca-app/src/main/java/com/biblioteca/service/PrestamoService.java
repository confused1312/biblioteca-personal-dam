package com.biblioteca.service;

import com.biblioteca.dao.AmigoDAO;
import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.model.Amigo;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.util.ResultadoOperacion;

import java.time.LocalDate;

public class PrestamoService {

    private final LibroDAO libroDAO = new LibroDAO();
    private final AmigoDAO amigoDAO = new AmigoDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();

    public ResultadoOperacion prestarLibro(int idLibro, int idAmigo, LocalDate fechaPrevista, String notas) {
        Libro libro = libroDAO.buscarPorId(idLibro);
        if (libro == null) {
            return ResultadoOperacion.error("El libro indicado no existe.");
        }
        if (!libro.isDisponible()) {
            return ResultadoOperacion.error("El libro no esta disponible para prestar.");
        }

        Amigo amigo = amigoDAO.buscarPorId(idAmigo);
        if (amigo == null) {
            return ResultadoOperacion.error("El amigo indicado no existe.");
        }

        if (fechaPrevista == null || fechaPrevista.isBefore(LocalDate.now())) {
            return ResultadoOperacion.error("La fecha prevista de devolucion no puede estar en el pasado.");
        }

        Prestamo p = new Prestamo(idLibro, idAmigo, LocalDate.now(), fechaPrevista, null, notas);
        if (!prestamoDAO.insertar(p)) {
            return ResultadoOperacion.error("Error al registrar el prestamo en la base de datos.");
        }

        libroDAO.cambiarDisponibilidad(idLibro, false);
        return ResultadoOperacion.exito("Prestamo registrado con ID " + p.getIdPrestamo() + ".");
    }

    public ResultadoOperacion devolverLibro(int idPrestamo) {
        Prestamo p = prestamoDAO.buscarPorId(idPrestamo);
        if (p == null) {
            return ResultadoOperacion.error("El prestamo indicado no existe.");
        }
        if (p.estaDevuelto()) {
            return ResultadoOperacion.error("Ese prestamo ya estaba devuelto.");
        }

        if (!prestamoDAO.registrarDevolucion(idPrestamo, LocalDate.now())) {
            return ResultadoOperacion.error("Error al registrar la devolucion.");
        }

        libroDAO.cambiarDisponibilidad(p.getIdLibro(), true);
        return ResultadoOperacion.exito("Devolucion registrada correctamente.");
    }
}