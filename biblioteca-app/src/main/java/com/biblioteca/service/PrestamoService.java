package com.biblioteca.service;

import com.biblioteca.dao.AmigoDAO;
import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.model.Amigo;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.util.ConexionBD;
import com.biblioteca.util.ResultadoOperacion;

import java.sql.Connection;
import java.sql.SQLException;
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

        Connection con = null;
        try {
            con = ConexionBD.conectar();
            con.setAutoCommit(false);

            Prestamo p = new Prestamo(idLibro, idAmigo, LocalDate.now(), fechaPrevista, null, notas);

            if (!prestamoDAO.insertar(p, con)) {
                con.rollback();
                return ResultadoOperacion.error("Error al registrar el prestamo en la base de datos.");
            }

            if (!libroDAO.cambiarDisponibilidad(idLibro, false, con)) {
                con.rollback();
                return ResultadoOperacion.error("Error al actualizar la disponibilidad del libro. Cambios revertidos.");
            }

            con.commit();
            return ResultadoOperacion.exito("Prestamo registrado con ID " + p.getIdPrestamo() + ".");

        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ignored) {}
            return ResultadoOperacion.error("Error en la transaccion: " + e.getMessage());
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (SQLException ignored) {}
        }
    }

    public ResultadoOperacion devolverLibro(int idPrestamo) {
        Prestamo p = prestamoDAO.buscarPorId(idPrestamo);
        if (p == null) {
            return ResultadoOperacion.error("El prestamo indicado no existe.");
        }
        if (p.estaDevuelto()) {
            return ResultadoOperacion.error("Ese prestamo ya estaba devuelto.");
        }

        Connection con = null;
        try {
            con = ConexionBD.conectar();
            con.setAutoCommit(false);

            if (!prestamoDAO.registrarDevolucion(idPrestamo, LocalDate.now(), con)) {
                con.rollback();
                return ResultadoOperacion.error("Error al registrar la devolucion.");
            }

            if (!libroDAO.cambiarDisponibilidad(p.getIdLibro(), true, con)) {
                con.rollback();
                return ResultadoOperacion.error("Error al actualizar la disponibilidad del libro. Cambios revertidos.");
            }

            con.commit();
            return ResultadoOperacion.exito("Devolucion registrada correctamente.");

        } catch (SQLException e) {
            try { if (con != null) con.rollback(); } catch (SQLException ignored) {}
            return ResultadoOperacion.error("Error en la transaccion: " + e.getMessage());
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (SQLException ignored) {}
        }
    }
}