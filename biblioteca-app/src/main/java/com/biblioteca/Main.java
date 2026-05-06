package com.biblioteca;

import com.biblioteca.util.ConexionBD;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection con = null;
        try {
            con = ConexionBD.conectar();
            System.out.println("Conexion establecida correctamente con la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error de conexion: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(con);
        }
    }
}