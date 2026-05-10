package com.biblioteca.dao;

import com.biblioteca.model.Prestamo;
import com.biblioteca.util.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO implements Dao<Prestamo> {

    @Override
    public boolean insertar(Prestamo prestamo) {
        try (Connection con = ConexionBD.conectar()) {
            return insertar(prestamo, con);
        } catch (SQLException e) {
            System.err.println("Error al insertar prestamo: " + e.getMessage());
            return false;
        }
    }

    public boolean insertar(Prestamo prestamo, Connection con) throws SQLException {
        String sql = "INSERT INTO prestamo (id_libro, id_amigo, fecha_prestamo, fecha_devolucion_prevista, fecha_devolucion_real, notas) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, prestamo.getIdLibro());
            ps.setInt(2, prestamo.getIdAmigo());
            ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucionPrevista()));
            if (prestamo.getFechaDevolucionReal() != null) {
                ps.setDate(5, Date.valueOf(prestamo.getFechaDevolucionReal()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setString(6, prestamo.getNotas());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) prestamo.setIdPrestamo(rs.getInt(1));
                return true;
            }
            return false;
        }
    }

    @Override
    public Prestamo buscarPorId(int id) {
        String sql = "SELECT * FROM prestamo WHERE id_prestamo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al buscar prestamo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Prestamo> listarTodos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo ORDER BY fecha_prestamo DESC";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar prestamos: " + e.getMessage());
        }
        return lista;
    }

    public List<Prestamo> listarActivos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo WHERE fecha_devolucion_real IS NULL ORDER BY fecha_devolucion_prevista";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar prestamos activos: " + e.getMessage());
        }
        return lista;
    }

    public List<Prestamo> listarVencidos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamo WHERE fecha_devolucion_real IS NULL AND fecha_devolucion_prevista < CURDATE() " +
                     "ORDER BY fecha_devolucion_prevista";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar prestamos vencidos: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrarDevolucion(int idPrestamo, LocalDate fechaDevolucion, Connection con) throws SQLException {
        String sql = "UPDATE prestamo SET fecha_devolucion_real = ? WHERE id_prestamo = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaDevolucion));
            ps.setInt(2, idPrestamo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean registrarDevolucion(int idPrestamo, LocalDate fechaDevolucion) {
        try (Connection con = ConexionBD.conectar()) {
            return registrarDevolucion(idPrestamo, fechaDevolucion, con);
        } catch (SQLException e) {
            System.err.println("Error al registrar devolucion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Prestamo prestamo) {
        String sql = "UPDATE prestamo SET id_libro=?, id_amigo=?, fecha_prestamo=?, fecha_devolucion_prevista=?, " +
                     "fecha_devolucion_real=?, notas=? WHERE id_prestamo=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, prestamo.getIdLibro());
            ps.setInt(2, prestamo.getIdAmigo());
            ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucionPrevista()));
            if (prestamo.getFechaDevolucionReal() != null) {
                ps.setDate(5, Date.valueOf(prestamo.getFechaDevolucionReal()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setString(6, prestamo.getNotas());
            ps.setInt(7, prestamo.getIdPrestamo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar prestamo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM prestamo WHERE id_prestamo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar prestamo: " + e.getMessage());
            return false;
        }
    }

    private Prestamo mapear(ResultSet rs) throws SQLException {
        Date fechaReal = rs.getDate("fecha_devolucion_real");
        return new Prestamo(
                rs.getInt("id_prestamo"),
                rs.getInt("id_libro"),
                rs.getInt("id_amigo"),
                rs.getDate("fecha_prestamo").toLocalDate(),
                rs.getDate("fecha_devolucion_prevista").toLocalDate(),
                fechaReal != null ? fechaReal.toLocalDate() : null,
                rs.getString("notas")
        );
    }
}