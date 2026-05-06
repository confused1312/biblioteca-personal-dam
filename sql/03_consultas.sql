-- ============================================================================
-- PROYECTO INTERMODULAR DAM - BIBLIOTECA PERSONAL
-- Script: 03_consultas.sql
-- Descripción: Conjunto de consultas SQL representativas del uso real de la
--              aplicación. Cubre SELECT con filtros, JOINs, funciones
--              agregadas, GROUP BY, subconsultas y búsquedas.
-- Autor: David Jamiro López Wissroth
-- Módulo: 0484 - Bases de Datos
-- ============================================================================

USE biblioteca_personal;

-- ============================================================================
-- 1. CONSULTAS BÁSICAS (SELECT con filtros y ordenación)
-- ============================================================================

-- 1.1. Listar todos los libros disponibles ordenados por título.
SELECT id_libro, titulo, isbn, anio_publicacion
FROM libro
WHERE disponible = TRUE
ORDER BY titulo ASC;

-- 1.2. Listar los libros NO disponibles (actualmente prestados).
SELECT id_libro, titulo, isbn
FROM libro
WHERE disponible = FALSE;

-- 1.3. Buscar libros publicados antes del año 1950.
SELECT titulo, anio_publicacion, editorial
FROM libro
WHERE anio_publicacion < 1950
ORDER BY anio_publicacion ASC;

-- 1.4. Buscar autores españoles o argentinos.
SELECT nombre, apellidos, nacionalidad
FROM autor
WHERE nacionalidad IN ('Española', 'Argentina');

-- ============================================================================
-- 2. BÚSQUEDAS CON LIKE (búsqueda parcial de texto)
-- ============================================================================

-- 2.1. Buscar libros cuyo título contenga la palabra "amor".
SELECT titulo, editorial
FROM libro
WHERE titulo LIKE '%amor%';

-- 2.2. Buscar amigos cuyo apellido empiece por "M".
SELECT nombre, apellidos, email
FROM amigo
WHERE apellidos LIKE 'M%';

-- ============================================================================
-- 3. JOINS (consultas multitabla)
-- ============================================================================

-- 3.1. Listar todos los libros con el nombre completo de su autor y categoría.
SELECT
    l.titulo,
    CONCAT(a.nombre, ' ', a.apellidos) AS autor,
    c.nombre AS categoria,
    l.anio_publicacion
FROM libro l
JOIN autor a       ON l.id_autor     = a.id_autor
JOIN categoria c   ON l.id_categoria = c.id_categoria
ORDER BY a.apellidos, l.titulo;

-- 3.2. Mostrar los préstamos activos (sin devolver) con el libro y el amigo.
SELECT
    p.id_prestamo,
    l.titulo                                AS libro,
    CONCAT(am.nombre, ' ', am.apellidos)    AS amigo,
    p.fecha_prestamo,
    p.fecha_devolucion_prevista
FROM prestamo p
JOIN libro l   ON p.id_libro = l.id_libro
JOIN amigo am  ON p.id_amigo = am.id_amigo
WHERE p.fecha_devolucion_real IS NULL
ORDER BY p.fecha_devolucion_prevista ASC;

-- 3.3. Préstamos VENCIDOS: sin devolver y con fecha prevista pasada.
SELECT
    l.titulo                                AS libro,
    CONCAT(am.nombre, ' ', am.apellidos)    AS amigo,
    am.email,
    am.telefono,
    p.fecha_prestamo,
    p.fecha_devolucion_prevista,
    DATEDIFF(CURDATE(), p.fecha_devolucion_prevista) AS dias_de_retraso
FROM prestamo p
JOIN libro l   ON p.id_libro = l.id_libro
JOIN amigo am  ON p.id_amigo = am.id_amigo
WHERE p.fecha_devolucion_real IS NULL
  AND p.fecha_devolucion_prevista < CURDATE()
ORDER BY dias_de_retraso DESC;

-- 3.4. Historial completo de préstamos (devueltos y activos) ordenado por
--      fecha de préstamo descendente.
SELECT
    l.titulo,
    CONCAT(am.nombre, ' ', am.apellidos) AS amigo,
    p.fecha_prestamo,
    p.fecha_devolucion_real,
    CASE
        WHEN p.fecha_devolucion_real IS NULL THEN 'Activo'
        WHEN p.fecha_devolucion_real <= p.fecha_devolucion_prevista THEN 'Devuelto a tiempo'
        ELSE 'Devuelto con retraso'
    END AS estado
FROM prestamo p
JOIN libro l   ON p.id_libro = l.id_libro
JOIN amigo am  ON p.id_amigo = am.id_amigo
ORDER BY p.fecha_prestamo DESC;

-- ============================================================================
-- 4. FUNCIONES AGREGADAS Y GROUP BY
-- ============================================================================

-- 4.1. Contar cuántos libros hay en cada categoría.
SELECT
    c.nombre AS categoria,
    COUNT(l.id_libro) AS total_libros
FROM categoria c
LEFT JOIN libro l ON c.id_categoria = l.id_categoria
GROUP BY c.id_categoria, c.nombre
ORDER BY total_libros DESC;

-- 4.2. Contar cuántos libros ha escrito cada autor (solo autores con libros).
SELECT
    CONCAT(a.nombre, ' ', a.apellidos) AS autor,
    COUNT(l.id_libro) AS total_libros
FROM autor a
JOIN libro l ON a.id_autor = l.id_autor
GROUP BY a.id_autor, a.nombre, a.apellidos
ORDER BY total_libros DESC;

-- 4.3. Estadísticas generales de la colección.
SELECT
    COUNT(*)                 AS total_libros,
    AVG(num_paginas)         AS promedio_paginas,
    MAX(num_paginas)         AS libro_mas_largo,
    MIN(num_paginas)         AS libro_mas_corto,
    MIN(anio_publicacion)    AS publicacion_mas_antigua,
    MAX(anio_publicacion)    AS publicacion_mas_reciente
FROM libro;

-- 4.4. Top 3 amigos que más libros han pedido prestados (incluyendo
--      préstamos pasados y actuales).
SELECT
    CONCAT(am.nombre, ' ', am.apellidos) AS amigo,
    COUNT(p.id_prestamo) AS total_prestamos
FROM amigo am
JOIN prestamo p ON am.id_amigo = p.id_amigo
GROUP BY am.id_amigo, am.nombre, am.apellidos
ORDER BY total_prestamos DESC
LIMIT 3;

-- ============================================================================
-- 5. HAVING (filtrado sobre agregados)
-- ============================================================================

-- 5.1. Categorías que tienen más de 2 libros.
SELECT
    c.nombre AS categoria,
    COUNT(l.id_libro) AS total
FROM categoria c
JOIN libro l ON c.id_categoria = l.id_categoria
GROUP BY c.id_categoria, c.nombre
HAVING COUNT(l.id_libro) > 2;

-- ============================================================================
-- 6. SUBCONSULTAS
-- ============================================================================

-- 6.1. Listar libros cuyo número de páginas está por encima del promedio.
SELECT titulo, num_paginas
FROM libro
WHERE num_paginas > (SELECT AVG(num_paginas) FROM libro)
ORDER BY num_paginas DESC;

-- 6.2. Mostrar autores que NUNCA han tenido un libro prestado.
SELECT CONCAT(a.nombre, ' ', a.apellidos) AS autor
FROM autor a
WHERE a.id_autor NOT IN (
    SELECT DISTINCT l.id_autor
    FROM libro l
    JOIN prestamo p ON l.id_libro = p.id_libro
);

-- ============================================================================
-- FIN DE CONSULTAS
-- ============================================================================
