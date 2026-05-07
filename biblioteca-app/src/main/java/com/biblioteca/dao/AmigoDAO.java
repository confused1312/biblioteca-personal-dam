package com.biblioteca.dao;

import com.biblioteca.model.Amigo;
import com.biblioteca.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AmigoDAO {

    public boolean insertar(Amigo amigo) {
        String sql = "INSERT INTO amigo (nombre, apellidos, email, telefono) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, amigo.getNombre());
            ps.setString(2, amigo.getApellidos());
            ps.setString(3, amigo.getEmail());
            ps.setString(4, amigo.getTelefono());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) amigo.setIdAmigo(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error al insertar amigo: " + e.getMessage());
            return false;
        }
    }

    public Amigo buscarPorId(int id) {
        String sql = "SELECT * FROM amigo WHERE id_amigo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar amigo: " + e.getMessage());
        }
        return null;
    }

    public List<Amigo> listarTodos() {
        List<Amigo> lista = new ArrayList<>();
        String sql = "SELECT * FROM amigo ORDER BY apellidos, nombre";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar amigos: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Amigo amigo) {
        String sql = "UPDATE amigo SET nombre = ?, apellidos = ?, email = ?, telefono = ? WHERE id_amigo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, amigo.getNombre());
            ps.setString(2, amigo.getApellidos());
            ps.setString(3, amigo.getEmail());
            ps.setString(4, amigo.getTelefono());
            ps.setInt(5, amigo.getIdAmigo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar amigo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM amigo WHERE id_amigo = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar amigo: " + e.getMessage());
            return false;
        }
    }

    private Amigo mapear(ResultSet rs) throws SQLException {
        return new Amigo(
                rs.getInt("id_amigo"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("email"),
                rs.getString("telefono")
        );
    }
}
