-- ============================================================================
-- PROYECTO INTERMODULAR DAM - BIBLIOTECA PERSONAL
-- Script: 02_insertar_datos.sql
-- Descripción: Inserción de datos de ejemplo en todas las tablas.
--              Estos datos permiten probar la aplicación con un volumen
--              razonable y casos variados (libros disponibles, prestados,
--              devueltos, vencidos, etc.).
-- Autor: David Jamiro López Wissroth
-- Módulo: 0484 - Bases de Datos
-- ============================================================================

USE biblioteca_personal;

-- Limpiar las tablas antes de insertar (por si se ejecuta varias veces).
-- El orden es importante: primero las tablas hijas, después las padres.
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE prestamo;
TRUNCATE TABLE libro;
TRUNCATE TABLE amigo;
TRUNCATE TABLE categoria;
TRUNCATE TABLE autor;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- INSERCIÓN DE AUTORES
-- ============================================================================
INSERT INTO autor (nombre, apellidos, nacionalidad, anio_nacimiento) VALUES
    ('Gabriel',     'García Márquez',          'Colombiana',     1927),
    ('Isabel',      'Allende',                 'Chilena',        1942),
    ('Miguel',      'de Cervantes',            'Española',       1547),
    ('Jorge Luis',  'Borges',                  'Argentina',      1899),
    ('Mario',       'Vargas Llosa',            'Peruana',        1936),
    ('George',      'Orwell',                  'Británica',      1903),
    ('J.R.R.',      'Tolkien',                 'Británica',      1892),
    ('Stephen',     'King',                    'Estadounidense', 1947),
    ('Agatha',      'Christie',                'Británica',      1890),
    ('Haruki',      'Murakami',                'Japonesa',       1949);

-- ============================================================================
-- INSERCIÓN DE CATEGORIAS
-- ============================================================================
INSERT INTO categoria (nombre, descripcion) VALUES
    ('Novela',           'Narrativa de ficción de extensión larga.'),
    ('Ciencia Ficción',  'Obras basadas en avances científicos imaginarios.'),
    ('Fantasía',         'Narrativa con elementos sobrenaturales o mágicos.'),
    ('Misterio',         'Tramas centradas en enigmas y casos por resolver.'),
    ('Terror',           'Obras destinadas a provocar miedo o inquietud.'),
    ('Ensayo',           'Textos reflexivos sobre un tema específico.'),
    ('Clásico',          'Obras de la literatura considerada universal.'),
    ('Poesía',           'Composiciones líricas en verso.');

-- ============================================================================
-- INSERCIÓN DE LIBROS
-- Nota: los IDs de autor y categoría siguen el orden de inserción anterior.
-- ============================================================================
INSERT INTO libro (titulo, isbn, anio_publicacion, num_paginas, editorial, id_autor, id_categoria, disponible) VALUES
    ('Cien años de soledad',          '978-8497592208', 1967, 471, 'Cátedra',           1, 1, TRUE),
    ('El amor en los tiempos del cólera', '978-8439728375', 1985, 496, 'Debolsillo',     1, 1, FALSE),
    ('La casa de los espíritus',      '978-8401242502', 1982, 480, 'Plaza & Janés',     2, 1, TRUE),
    ('Don Quijote de la Mancha',      '978-8424116378', 1605, 1376,'Espasa',            3, 7, TRUE),
    ('Ficciones',                     '978-8420633138', 1944, 224, 'Alianza Editorial', 4, 7, TRUE),
    ('La ciudad y los perros',        '978-8420471839', 1963, 432, 'Alfaguara',         5, 1, FALSE),
    ('1984',                          '978-8499890944', 1949, 352, 'Debolsillo',        6, 2, TRUE),
    ('Rebelión en la granja',         '978-8499890951', 1945, 152, 'Debolsillo',        6, 1, TRUE),
    ('El señor de los anillos',       '978-8445000663', 1954, 1392,'Minotauro',         7, 3, FALSE),
    ('El hobbit',                     '978-8445000694', 1937, 320, 'Minotauro',         7, 3, TRUE),
    ('It',                            '978-8401021800', 1986, 1504,'Debolsillo',        8, 5, TRUE),
    ('El resplandor',                 '978-8497594257', 1977, 656, 'Debolsillo',        8, 5, TRUE),
    ('Diez negritos',                 '978-8467033908', 1939, 256, 'Booket',            9, 4, FALSE),
    ('Asesinato en el Orient Express','978-8467034035', 1934, 288, 'Booket',            9, 4, TRUE),
    ('Tokio blues',                   '978-8483462959', 1987, 384, 'Tusquets',         10, 1, TRUE);

-- ============================================================================
-- INSERCIÓN DE AMIGOS
-- ============================================================================
INSERT INTO amigo (nombre, apellidos, email, telefono) VALUES
    ('Laura',   'Martínez Pérez',    'laura.martinez@email.com',    '600111222'),
    ('Carlos',  'Sánchez López',     'carlos.sanchez@email.com',    '600333444'),
    ('María',   'Rodríguez García',  'maria.rodriguez@email.com',   '600555666'),
    ('Javier',  'Fernández Ruiz',    'javier.fernandez@email.com',  '600777888'),
    ('Ana',     'González Jiménez',  'ana.gonzalez@email.com',      '600999000'),
    ('Daniel',  'López Moreno',      'daniel.lopez@email.com',      '611222333');

-- ============================================================================
-- INSERCIÓN DE PRÉSTAMOS
-- Casos cubiertos:
--   - Préstamos devueltos a tiempo.
--   - Préstamos activos (sin devolver) dentro del plazo.
--   - Préstamos vencidos (sin devolver y con fecha de devolución pasada).
--   - Préstamos devueltos con retraso.
-- ============================================================================
INSERT INTO prestamo (id_libro, id_amigo, fecha_prestamo, fecha_devolucion_prevista, fecha_devolucion_real, notas) VALUES
    -- Préstamos devueltos correctamente
    (1, 1, '2026-01-10', '2026-01-31', '2026-01-28', 'Devuelto en buen estado.'),
    (4, 2, '2026-02-01', '2026-02-28', '2026-02-25', 'Le encantó.'),
    (10, 3, '2026-02-15', '2026-03-15', '2026-03-10', NULL),

    -- Préstamos actualmente activos (libros marcados como NO disponibles)
    (2, 4, '2026-04-15', '2026-05-15', NULL, 'Lo necesita para un trabajo.'),
    (6, 5, '2026-04-20', '2026-05-20', NULL, NULL),
    (9, 1, '2026-04-25', '2026-05-25', NULL, 'Saga completa, leer con tiempo.'),

    -- Préstamo vencido (sin devolver y fecha prevista pasada)
    (13, 6, '2026-03-01', '2026-04-01', NULL, 'Pendiente de reclamar.'),

    -- Préstamo devuelto con retraso
    (5, 2, '2025-11-01', '2025-12-01', '2025-12-20', 'Devuelto con retraso.');

-- ============================================================================
-- VERIFICACIÓN
-- Mostrar el conteo de registros en cada tabla.
-- ============================================================================
SELECT 'autor'     AS tabla, COUNT(*) AS total FROM autor
UNION ALL
SELECT 'categoria' AS tabla, COUNT(*) AS total FROM categoria
UNION ALL
SELECT 'libro'     AS tabla, COUNT(*) AS total FROM libro
UNION ALL
SELECT 'amigo'     AS tabla, COUNT(*) AS total FROM amigo
UNION ALL
SELECT 'prestamo'  AS tabla, COUNT(*) AS total FROM prestamo;
