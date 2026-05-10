package com.biblioteca.dao;

import java.util.List;

public interface Dao<T> {

    boolean insertar(T entidad);

    T buscarPorId(int id);

    List<T> listarTodos();

    boolean actualizar(T entidad);

    boolean eliminar(int id);
}