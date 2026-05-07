package com.biblioteca.util;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExportadorXML {

    public boolean exportarCatalogo(String rutaSalida, String propietario) {
        String sql = "SELECT l.id_libro, l.titulo, l.isbn, l.editorial, l.anio_publicacion, " +
                     "l.num_paginas, l.disponible, " +
                     "CONCAT(a.nombre, ' ', a.apellidos) AS autor, c.nombre AS categoria " +
                     "FROM libro l " +
                     "JOIN autor a ON l.id_autor = a.id_autor " +
                     "JOIN categoria c ON l.id_categoria = c.id_categoria " +
                     "ORDER BY l.id_libro";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();
             Writer w = new OutputStreamWriter(new FileOutputStream(rutaSalida), StandardCharsets.UTF_8)) {

            int total = contarLibros();

            w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            w.write("<catalogo xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
            w.write("          xsi:noNamespaceSchemaLocation=\"catalogo.xsd\">\n\n");

            w.write("    <info>\n");
            w.write("        <propietario>" + escapar(propietario) + "</propietario>\n");
            w.write("        <fechaExportacion>" + LocalDate.now() + "</fechaExportacion>\n");
            w.write("        <totalLibros>" + total + "</totalLibros>\n");
            w.write("    </info>\n\n");

            w.write("    <libros>\n");

            while (rs.next()) {
                int id = rs.getInt("id_libro");
                String titulo = rs.getString("titulo");
                String isbn = rs.getString("isbn");
                String autor = rs.getString("autor");
                String categoria = rs.getString("categoria");
                String editorial = rs.getString("editorial");
                int anio = rs.getInt("anio_publicacion");
                boolean tieneAnio = !rs.wasNull();
                int paginas = rs.getInt("num_paginas");
                boolean tienePaginas = !rs.wasNull();
                boolean disponible = rs.getBoolean("disponible");

                w.write("        <libro id=\"" + id + "\">\n");
                w.write("            <titulo>" + escapar(titulo) + "</titulo>\n");
                w.write("            <isbn>" + escapar(isbn) + "</isbn>\n");
                w.write("            <autor>" + escapar(autor) + "</autor>\n");
                w.write("            <categoria>" + escapar(categoria) + "</categoria>\n");
                if (editorial != null && !editorial.isBlank()) {
                    w.write("            <editorial>" + escapar(editorial) + "</editorial>\n");
                }
                if (tieneAnio) {
                    w.write("            <anioPublicacion>" + anio + "</anioPublicacion>\n");
                }
                if (tienePaginas) {
                    w.write("            <numPaginas>" + paginas + "</numPaginas>\n");
                }
                w.write("            <disponible>" + disponible + "</disponible>\n");
                w.write("        </libro>\n");
            }

            w.write("    </libros>\n");
            w.write("</catalogo>\n");

            return true;
        } catch (SQLException | java.io.IOException e) {
            System.err.println("Error al exportar XML: " + e.getMessage());
            return false;
        }
    }

    private int contarLibros() {
        String sql = "SELECT COUNT(*) FROM libro";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }

    private String escapar(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}