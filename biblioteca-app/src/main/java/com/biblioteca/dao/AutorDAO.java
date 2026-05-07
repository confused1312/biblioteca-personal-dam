package com.biblioteca.dao;

import com.biblioteca.model.Autor;
import com.biblioteca.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutorDAO {

    public boolean insertar(Autor autor) {
        String sql = "INSERT INTO autor (nombre, apellidos, nacionalidad, anio_nacimiento) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, autor.getNombre());
            ps.setString(2, autor.getApellidos());
            ps.setString(3, autor.getNacionalidad());
            if (autor.getAnioNacimiento() != null) {
                ps.setInt(4, autor.getAnioNacimiento());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) autor.setIdAutor(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar autor: " + e.getMessage());
            return false;
        }
    }

    public Autor buscarPorId(int id) {
        String sql = "SELECT * FROM autor WHERE id_autor = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar autor: " + e.getMessage());
        }
        return null;
    }

    public List<Autor> listarTodos() {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM autor ORDER BY apellidos, nombre";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar autores: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Autor autor) {
        String sql = "UPDATE autor SET nombre = ?, apellidos = ?, nacionalidad = ?, anio_nacimiento = ? WHERE id_autor = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, autor.getNombre());
            ps.setString(2, autor.getApellidos());
            ps.setString(3, autor.getNacionalidad());
            if (autor.getAnioNacimiento() != null) {
                ps.setInt(4, autor.getAnioNacimiento());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, autor.getIdAutor());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar autor: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM autor WHERE id_autor = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar autor: " + e.getMessage());
            return false;
        }
    }

    private Autor mapear(ResultSet rs) throws SQLException {
        Integer anio = rs.getInt("anio_nacimiento");
        if (rs.wasNull()) anio = null;
        return new Autor(
                rs.getInt("id_autor"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("nacionalidad"),
                anio
        );
    }
}
