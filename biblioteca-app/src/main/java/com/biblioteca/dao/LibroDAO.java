package com.biblioteca.dao;

import com.biblioteca.model.Libro;
import com.biblioteca.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public boolean insertar(Libro libro) {
        String sql = "INSERT INTO libro (titulo, isbn, anio_publicacion, num_paginas, editorial, id_autor, id_categoria, disponible) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getIsbn());
            if (libro.getAnioPublicacion() != null) ps.setInt(3, libro.getAnioPublicacion());
            else ps.setNull(3, Types.INTEGER);
            if (libro.getNumPaginas() != null) ps.setInt(4, libro.getNumPaginas());
            else ps.setNull(4, Types.INTEGER);
            ps.setString(5, libro.getEditorial());
            ps.setInt(6, libro.getIdAutor());
            ps.setInt(7, libro.getIdCategoria());
            ps.setBoolean(8, libro.isDisponible());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) libro.setIdLibro(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            return false;
        }
    }

    public Libro buscarPorId(int id) {
        String sql = "SELECT * FROM libro WHERE id_libro = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al buscar libro: " + e.getMessage());
        }
        return null;
    }

    public List<Libro> listarTodos() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libro ORDER BY titulo";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }
        return lista;
    }

    public List<Libro> listarDisponibles() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libro WHERE disponible = TRUE ORDER BY titulo";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar libros disponibles: " + e.getMessage());
        }
        return lista;
    }

    public List<Libro> buscarPorTitulo(String texto) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libro WHERE titulo LIKE ? ORDER BY titulo";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + texto + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al buscar libros: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libro SET titulo=?, isbn=?, anio_publicacion=?, num_paginas=?, editorial=?, " +
                     "id_autor=?, id_categoria=?, disponible=? WHERE id_libro=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getIsbn());
            if (libro.getAnioPublicacion() != null) ps.setInt(3, libro.getAnioPublicacion());
            else ps.setNull(3, Types.INTEGER);
            if (libro.getNumPaginas() != null) ps.setInt(4, libro.getNumPaginas());
            else ps.setNull(4, Types.INTEGER);
            ps.setString(5, libro.getEditorial());
            ps.setInt(6, libro.getIdAutor());
            ps.setInt(7, libro.getIdCategoria());
            ps.setBoolean(8, libro.isDisponible());
            ps.setInt(9, libro.getIdLibro());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM libro WHERE id_libro = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarDisponibilidad(int idLibro, boolean disponible) {
        String sql = "UPDATE libro SET disponible = ? WHERE id_libro = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, disponible);
            ps.setInt(2, idLibro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar disponibilidad: " + e.getMessage());
            return false;
        }
    }

    private Libro mapear(ResultSet rs) throws SQLException {
        Integer anio = rs.getInt("anio_publicacion");
        if (rs.wasNull()) anio = null;
        Integer paginas = rs.getInt("num_paginas");
        if (rs.wasNull()) paginas = null;

        return new Libro(
                rs.getInt("id_libro"),
                rs.getString("titulo"),
                rs.getString("isbn"),
                anio,
                paginas,
                rs.getString("editorial"),
                rs.getInt("id_autor"),
                rs.getInt("id_categoria"),
                rs.getBoolean("disponible")
        );
    }
}
