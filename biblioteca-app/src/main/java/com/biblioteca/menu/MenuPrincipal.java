package com.biblioteca.menu;

import com.biblioteca.dao.*;
import com.biblioteca.model.*;
import com.biblioteca.service.PrestamoService;
import com.biblioteca.util.ExportadorXML;
import com.biblioteca.util.ResultadoOperacion;
import com.biblioteca.util.Validador;
import com.biblioteca.util.ValidadorXML;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner sc = new Scanner(System.in);
    private final AutorDAO autorDAO = new AutorDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final LibroDAO libroDAO = new LibroDAO();
    private final AmigoDAO amigoDAO = new AmigoDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final PrestamoService prestamoService = new PrestamoService();
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Opcion: ");
            switch (opcion) {
                case 1 -> menuLibros();
                case 2 -> menuAutores();
                case 3 -> menuCategorias();
                case 4 -> menuAmigos();
                case 5 -> menuPrestamos();
                case 6 -> exportarCatalogo();
                case 0 -> {
                    System.out.println("Hasta luego.");
                    salir = true;
                }
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("=== BIBLIOTECA PERSONAL ===");
        System.out.println("1. Libros");
        System.out.println("2. Autores");
        System.out.println("3. Categorias");
        System.out.println("4. Amigos");
        System.out.println("5. Prestamos");
        System.out.println("6. Exportar catalogo a XML");
        System.out.println("0. Salir");
    }

    private void exportarCatalogo() {
        String propietario = leerTextoValidado("Tu nombre: ", "El nombre no puede estar vacio.");
        String rutaXml = "C:\\proyectos\\biblioteca-personal-dam\\xml\\catalogo.xml";
        String rutaXsd = "C:\\proyectos\\biblioteca-personal-dam\\xml\\catalogo.xsd";

        ExportadorXML exp = new ExportadorXML();
        if (!exp.exportarCatalogo(rutaXml, propietario)) {
            System.out.println("Error en la exportacion.");
            return;
        }
        System.out.println("Catalogo exportado a: " + rutaXml);

        ValidadorXML val = new ValidadorXML();
        if (val.validar(rutaXml, rutaXsd)) {
            System.out.println("XML validado correctamente contra el XSD.");
        } else {
            System.out.println("El XML generado NO pasa la validacion del XSD.");
        }
    }

    private void menuLibros() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Libros ---");
            System.out.println("1. Listar todos");
            System.out.println("2. Listar disponibles");
            System.out.println("3. Buscar por titulo");
            System.out.println("4. Anadir libro");
            System.out.println("5. Modificar libro");
            System.out.println("6. Eliminar libro");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> imprimirLista(libroDAO.listarTodos());
                case 2 -> imprimirLista(libroDAO.listarDisponibles());
                case 3 -> {
                    String t = leerTextoValidado("Texto a buscar: ", "El texto no puede estar vacio.");
                    imprimirLista(libroDAO.buscarPorTitulo(t));
                }
                case 4 -> anadirLibro();
                case 5 -> modificarLibro();
                case 6 -> eliminarLibro();
                case 0 -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void anadirLibro() {
        String titulo = leerTextoValidado("Titulo: ", "El titulo no puede estar vacio.");
        String isbn = leerIsbnValidado("ISBN: ");
        Integer anio = leerAnioOpcional("Anio publicacion (vacio para omitir): ");
        Integer paginas = leerEnteroPositivoOpcional("Numero de paginas (vacio para omitir): ");
        String editorial = leerTexto("Editorial: ");
        int idAutor = leerEntero("ID del autor: ");
        int idCategoria = leerEntero("ID de la categoria: ");

        Libro libro = new Libro(titulo, isbn, anio, paginas, editorial, idAutor, idCategoria, true);
        if (libroDAO.insertar(libro)) {
            System.out.println("Libro anadido con ID " + libro.getIdLibro());
        } else {
            System.out.println("No se ha podido anadir el libro.");
        }
    }

    private void modificarLibro() {
        int id = leerEntero("ID del libro a modificar: ");
        Libro libro = libroDAO.buscarPorId(id);
        if (libro == null) {
            System.out.println("Libro no encontrado.");
            return;
        }
        System.out.println("Datos actuales: " + libro);
        libro.setTitulo(leerTextoConDefecto("Titulo", libro.getTitulo()));
        libro.setIsbn(leerTextoConDefecto("ISBN", libro.getIsbn()));
        libro.setEditorial(leerTextoConDefecto("Editorial", libro.getEditorial()));

        if (libroDAO.actualizar(libro)) {
            System.out.println("Libro actualizado.");
        } else {
            System.out.println("No se pudo actualizar.");
        }
    }

    private void eliminarLibro() {
        int id = leerEntero("ID del libro a eliminar: ");
        if (libroDAO.eliminar(id)) {
            System.out.println("Libro eliminado.");
        } else {
            System.out.println("No se pudo eliminar (puede tener prestamos asociados).");
        }
    }

    private void menuAutores() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Autores ---");
            System.out.println("1. Listar");
            System.out.println("2. Anadir");
            System.out.println("3. Modificar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> imprimirLista(autorDAO.listarTodos());
                case 2 -> anadirAutor();
                case 3 -> modificarAutor();
                case 4 -> eliminarAutor();
                case 0 -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void anadirAutor() {
        String nombre = leerTextoValidado("Nombre: ", "El nombre no puede estar vacio.");
        String apellidos = leerTextoValidado("Apellidos: ", "Los apellidos no pueden estar vacios.");
        String nacionalidad = leerTexto("Nacionalidad: ");
        Integer anio = leerAnioOpcional("Anio nacimiento (vacio para omitir): ");

        Autor autor = new Autor(nombre, apellidos, nacionalidad, anio);
        if (autorDAO.insertar(autor)) {
            System.out.println("Autor anadido con ID " + autor.getIdAutor());
        } else {
            System.out.println("No se pudo anadir.");
        }
    }

    private void modificarAutor() {
        int id = leerEntero("ID del autor: ");
        Autor a = autorDAO.buscarPorId(id);
        if (a == null) {
            System.out.println("No encontrado.");
            return;
        }
        System.out.println("Actual: " + a);
        a.setNombre(leerTextoConDefecto("Nombre", a.getNombre()));
        a.setApellidos(leerTextoConDefecto("Apellidos", a.getApellidos()));
        a.setNacionalidad(leerTextoConDefecto("Nacionalidad", a.getNacionalidad()));
        if (autorDAO.actualizar(a)) System.out.println("Actualizado.");
        else System.out.println("No se pudo actualizar.");
    }

    private void eliminarAutor() {
        int id = leerEntero("ID del autor a eliminar: ");
        if (autorDAO.eliminar(id)) System.out.println("Eliminado.");
        else System.out.println("No se pudo eliminar.");
    }

    private void menuCategorias() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Categorias ---");
            System.out.println("1. Listar");
            System.out.println("2. Anadir");
            System.out.println("3. Modificar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> imprimirLista(categoriaDAO.listarTodas());
                case 2 -> anadirCategoria();
                case 3 -> modificarCategoria();
                case 4 -> eliminarCategoria();
                case 0 -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void anadirCategoria() {
        String nombre = leerTextoValidado("Nombre: ", "El nombre no puede estar vacio.");
        String descripcion = leerTexto("Descripcion: ");
        Categoria c = new Categoria(nombre, descripcion);
        if (categoriaDAO.insertar(c)) System.out.println("Categoria anadida con ID " + c.getIdCategoria());
        else System.out.println("No se pudo anadir.");
    }

    private void modificarCategoria() {
        int id = leerEntero("ID de la categoria: ");
        Categoria c = categoriaDAO.buscarPorId(id);
        if (c == null) {
            System.out.println("No encontrada.");
            return;
        }
        System.out.println("Actual: " + c);
        c.setNombre(leerTextoConDefecto("Nombre", c.getNombre()));
        c.setDescripcion(leerTextoConDefecto("Descripcion", c.getDescripcion()));
        if (categoriaDAO.actualizar(c)) System.out.println("Actualizada.");
        else System.out.println("No se pudo actualizar.");
    }

    private void eliminarCategoria() {
        int id = leerEntero("ID a eliminar: ");
        if (categoriaDAO.eliminar(id)) System.out.println("Eliminada.");
        else System.out.println("No se pudo eliminar.");
    }

    private void menuAmigos() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Amigos ---");
            System.out.println("1. Listar");
            System.out.println("2. Anadir");
            System.out.println("3. Modificar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> imprimirLista(amigoDAO.listarTodos());
                case 2 -> anadirAmigo();
                case 3 -> modificarAmigo();
                case 4 -> eliminarAmigo();
                case 0 -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void anadirAmigo() {
        String nombre = leerTextoValidado("Nombre: ", "El nombre no puede estar vacio.");
        String apellidos = leerTextoValidado("Apellidos: ", "Los apellidos no pueden estar vacios.");
        String email = leerEmailValidado("Email: ");
        String telefono = leerTelefonoValidado("Telefono: ");
        Amigo a = new Amigo(nombre, apellidos, email, telefono);
        if (amigoDAO.insertar(a)) System.out.println("Amigo anadido con ID " + a.getIdAmigo());
        else System.out.println("No se pudo anadir.");
    }

    private void modificarAmigo() {
        int id = leerEntero("ID del amigo: ");
        Amigo a = amigoDAO.buscarPorId(id);
        if (a == null) {
            System.out.println("No encontrado.");
            return;
        }
        System.out.println("Actual: " + a);
        a.setNombre(leerTextoConDefecto("Nombre", a.getNombre()));
        a.setApellidos(leerTextoConDefecto("Apellidos", a.getApellidos()));
        a.setEmail(leerTextoConDefecto("Email", a.getEmail()));
        a.setTelefono(leerTextoConDefecto("Telefono", a.getTelefono()));
        if (amigoDAO.actualizar(a)) System.out.println("Actualizado.");
        else System.out.println("No se pudo actualizar.");
    }

    private void eliminarAmigo() {
        int id = leerEntero("ID a eliminar: ");
        if (amigoDAO.eliminar(id)) System.out.println("Eliminado.");
        else System.out.println("No se pudo eliminar.");
    }

    private void menuPrestamos() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Prestamos ---");
            System.out.println("1. Listar todos");
            System.out.println("2. Listar activos");
            System.out.println("3. Listar vencidos");
            System.out.println("4. Prestar libro");
            System.out.println("5. Devolver libro");
            System.out.println("0. Volver");
            int op = leerEntero("Opcion: ");
            switch (op) {
                case 1 -> imprimirLista(prestamoDAO.listarTodos());
                case 2 -> imprimirLista(prestamoDAO.listarActivos());
                case 3 -> imprimirLista(prestamoDAO.listarVencidos());
                case 4 -> prestarLibro();
                case 5 -> devolverLibro();
                case 0 -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private void prestarLibro() {
        int idLibro = leerEntero("ID del libro a prestar: ");
        int idAmigo = leerEntero("ID del amigo: ");
        LocalDate fechaPrevista = leerFecha("Fecha devolucion prevista (yyyy-MM-dd): ");
        String notas = leerTexto("Notas (puede estar vacio): ");

        ResultadoOperacion r = prestamoService.prestarLibro(idLibro, idAmigo, fechaPrevista, notas);
        System.out.println(r.getMensaje());
    }

    private void devolverLibro() {
        int idPrestamo = leerEntero("ID del prestamo a devolver: ");
        ResultadoOperacion r = prestamoService.devolverLibro(idPrestamo);
        System.out.println(r.getMensaje());
    }

    private void imprimirLista(List<?> lista) {
        if (lista.isEmpty()) {
            System.out.println("(sin resultados)");
            return;
        }
        for (Object o : lista) {
            System.out.println(o);
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine();
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Numero invalido. Intentalo otra vez.");
            }
        }
    }

    private Integer leerEnteroOpcional(String mensaje) {
        System.out.print(mensaje);
        String linea = sc.nextLine().trim();
        if (linea.isEmpty()) return null;
        try {
            return Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer leerEnteroPositivoOpcional(String mensaje) {
        while (true) {
            Integer n = leerEnteroOpcional(mensaje);
            if (n == null) return null;
            if (Validador.esEnteroPositivo(n)) return n;
            System.out.println("Debe ser un numero positivo.");
        }
    }

    private Integer leerAnioOpcional(String mensaje) {
        while (true) {
            Integer n = leerEnteroOpcional(mensaje);
            if (n == null) return null;
            if (Validador.esAnioValido(n)) return n;
            System.out.println("Anio invalido. Debe estar entre 1000 y " + (LocalDate.now().getYear() + 1) + ".");
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    private String leerTextoValidado(String mensaje, String mensajeError) {
        while (true) {
            String t = leerTexto(mensaje);
            if (Validador.esTextoValido(t)) return t;
            System.out.println(mensajeError);
        }
    }

    private String leerIsbnValidado(String mensaje) {
        while (true) {
            String s = leerTexto(mensaje);
            if (Validador.esIsbnValido(s)) return s;
            System.out.println("ISBN invalido. Debe contener entre 10 y 20 caracteres (digitos y guiones).");
        }
    }

    private String leerEmailValidado(String mensaje) {
        while (true) {
            String s = leerTexto(mensaje);
            if (Validador.esEmailValido(s)) return s;
            System.out.println("Email invalido. Formato esperado: nombre@dominio.com");
        }
    }

    private String leerTelefonoValidado(String mensaje) {
        while (true) {
            String s = leerTexto(mensaje);
            if (Validador.esTelefonoValido(s)) return s;
            System.out.println("Telefono invalido. Solo digitos, espacios y opcionalmente '+' inicial. Entre 6 y 20 caracteres.");
        }
    }

    private String leerTextoConDefecto(String campo, String actual) {
        System.out.print(campo + " [" + actual + "]: ");
        String linea = sc.nextLine();
        return linea.isBlank() ? actual : linea;
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                return LocalDate.parse(linea, df);
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Usa yyyy-MM-dd (ej: 2026-06-15).");
            }
        }
    }
}