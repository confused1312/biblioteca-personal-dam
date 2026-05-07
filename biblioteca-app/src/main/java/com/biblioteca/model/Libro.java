package com.biblioteca.model;

public class Libro {
    private int idLibro;
    private String titulo;
    private String isbn;
    private Integer anioPublicacion;
    private Integer numPaginas;
    private String editorial;
    private int idAutor;
    private int idCategoria;
    private boolean disponible;

    public Libro() {}

    public Libro(String titulo, String isbn, Integer anioPublicacion, Integer numPaginas,
                 String editorial, int idAutor, int idCategoria, boolean disponible) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.numPaginas = numPaginas;
        this.editorial = editorial;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
        this.disponible = disponible;
    }

    public Libro(int idLibro, String titulo, String isbn, Integer anioPublicacion, Integer numPaginas,
                 String editorial, int idAutor, int idCategoria, boolean disponible) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.numPaginas = numPaginas;
        this.editorial = editorial;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
        this.disponible = disponible;
    }

    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(Integer anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public Integer getNumPaginas() { return numPaginas; }
    public void setNumPaginas(Integer numPaginas) { this.numPaginas = numPaginas; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return idLibro + " - " + titulo + " (" + (disponible ? "disponible" : "prestado") + ")";
    }
}
