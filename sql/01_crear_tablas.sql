-- ============================================================================
-- PROYECTO INTERMODULAR DAM - BIBLIOTECA PERSONAL
-- Script: 01_crear_tablas.sql
-- Descripción: Creación de la base de datos y todas las tablas del sistema.
-- Autor: David Jamiro López Wissroth
-- Módulo: 0484 - Bases de Datos
-- ============================================================================

-- Eliminar la base de datos si ya existe (para poder ejecutar el script
-- repetidamente durante el desarrollo sin acumular datos antiguos).
DROP DATABASE IF EXISTS biblioteca_personal;

-- Crear la base de datos con codificación UTF-8 para soportar tildes y eñes.
CREATE DATABASE biblioteca_personal
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Seleccionar la base de datos para que las siguientes sentencias se ejecuten
-- sobre ella.
USE biblioteca_personal;

-- ============================================================================
-- TABLA: AUTOR
-- Descripción: Almacena los autores de los libros.
-- ============================================================================
CREATE TABLE autor (
    id_autor          INT AUTO_INCREMENT PRIMARY KEY,
    nombre            VARCHAR(50)  NOT NULL,
    apellidos         VARCHAR(100) NOT NULL,
    nacionalidad      VARCHAR(50)  NULL,
    anio_nacimiento   INT          NULL
) ENGINE=InnoDB;

-- ============================================================================
-- TABLA: CATEGORIA
-- Descripción: Categorías o géneros literarios de los libros.
-- ============================================================================
CREATE TABLE categoria (
    id_categoria  INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(50)  NOT NULL UNIQUE,
    descripcion   VARCHAR(255) NULL
) ENGINE=InnoDB;

-- ============================================================================
-- TABLA: LIBRO
-- Descripción: Tabla central. Almacena los libros de la colección.
-- Relaciones:
--   - Cada libro pertenece a un AUTOR (FK id_autor).
--   - Cada libro pertenece a una CATEGORIA (FK id_categoria).
-- ============================================================================
CREATE TABLE libro (
    id_libro          INT AUTO_INCREMENT PRIMARY KEY,
    titulo            VARCHAR(150) NOT NULL,
    isbn              VARCHAR(20)  NOT NULL UNIQUE,
    anio_publicacion  INT          NULL,
    num_paginas       INT          NULL,
    editorial         VARCHAR(100) NULL,
    id_autor          INT          NOT NULL,
    id_categoria      INT          NOT NULL,
    disponible        BOOLEAN      NOT NULL DEFAULT TRUE,

    -- Definición de claves foráneas con integridad referencial.
    -- ON DELETE RESTRICT: no permite borrar un autor/categoría si tiene libros.
    -- ON UPDATE CASCADE: si cambia el id en la tabla padre, se actualiza aquí.
    CONSTRAINT fk_libro_autor
        FOREIGN KEY (id_autor) REFERENCES autor(id_autor)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_libro_categoria
        FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================================
-- TABLA: AMIGO
-- Descripción: Personas a las que se prestan libros.
-- ============================================================================
CREATE TABLE amigo (
    id_amigo    INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL,
    apellidos   VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NULL UNIQUE,
    telefono    VARCHAR(15)  NULL
) ENGINE=InnoDB;

-- ============================================================================
-- TABLA: PRESTAMO
-- Descripción: Tabla intermedia que registra cada préstamo realizado.
-- Resuelve la relación N:M entre LIBRO y AMIGO con atributos propios.
-- Relaciones:
--   - Cada préstamo es de un LIBRO (FK id_libro).
--   - Cada préstamo es a un AMIGO (FK id_amigo).
-- ============================================================================
CREATE TABLE prestamo (
    id_prestamo                 INT AUTO_INCREMENT PRIMARY KEY,
    id_libro                    INT          NOT NULL,
    id_amigo                    INT          NOT NULL,
    fecha_prestamo              DATE         NOT NULL,
    fecha_devolucion_prevista   DATE         NOT NULL,
    fecha_devolucion_real       DATE         NULL,
    notas                       VARCHAR(255) NULL,

    CONSTRAINT fk_prestamo_libro
        FOREIGN KEY (id_libro) REFERENCES libro(id_libro)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_prestamo_amigo
        FOREIGN KEY (id_amigo) REFERENCES amigo(id_amigo)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================================================
-- VERIFICACIÓN
-- Mostrar todas las tablas creadas para confirmar que el script se ha ejecutado
-- correctamente.
-- ============================================================================
SHOW TABLES;
