# Modelo Relacional - Biblioteca Personal

Este documento describe el modelo relacional derivado del diagrama Entidad-Relación de la base de datos del proyecto **Biblioteca Personal**.

El modelo relacional es la transformación del diseño conceptual (diagrama E/R) en estructuras concretas de tablas, listas para ser implementadas en un Sistema Gestor de Bases de Datos relacional como MySQL.

---

## Notación utilizada

Se utiliza la notación clásica del modelo relacional:

```
NOMBRE_TABLA (atributo1, atributo2, atributo3, ...)
   PK: clave_primaria
   FK: clave_foránea → TABLA_REFERENCIADA(atributo)
```

Donde:
- **PK** indica la clave primaria (Primary Key) de la tabla.
- **FK** indica una clave foránea (Foreign Key) que referencia a otra tabla.
- Los atributos subrayados o marcados como PK son únicos y no nulos por defecto.

---

## Tablas del modelo

### 1. AUTOR

Almacena la información de los autores de los libros.

```
AUTOR (id_autor, nombre, apellidos, nacionalidad, anio_nacimiento)
   PK: id_autor
```

| Atributo          | Tipo de dato  | Restricciones              |
|-------------------|---------------|----------------------------|
| id_autor          | INT           | PK, AUTO_INCREMENT, NOT NULL |
| nombre            | VARCHAR(50)   | NOT NULL                   |
| apellidos         | VARCHAR(100)  | NOT NULL                   |
| nacionalidad      | VARCHAR(50)   | NULL permitido             |
| anio_nacimiento   | INT           | NULL permitido             |

---

### 2. CATEGORIA

Almacena las distintas categorías o géneros literarios en los que se clasifican los libros.

```
CATEGORIA (id_categoria, nombre, descripcion)
   PK: id_categoria
```

| Atributo      | Tipo de dato  | Restricciones                |
|---------------|---------------|------------------------------|
| id_categoria  | INT           | PK, AUTO_INCREMENT, NOT NULL |
| nombre        | VARCHAR(50)   | NOT NULL, UNIQUE             |
| descripcion   | VARCHAR(255)  | NULL permitido               |

---

### 3. LIBRO

Tabla central del sistema. Almacena la información de cada libro de la colección personal.

```
LIBRO (id_libro, titulo, isbn, anio_publicacion, num_paginas, editorial, id_autor, id_categoria, disponible)
   PK: id_libro
   FK: id_autor → AUTOR(id_autor)
   FK: id_categoria → CATEGORIA(id_categoria)
```

| Atributo          | Tipo de dato  | Restricciones                          |
|-------------------|---------------|----------------------------------------|
| id_libro          | INT           | PK, AUTO_INCREMENT, NOT NULL           |
| titulo            | VARCHAR(150)  | NOT NULL                               |
| isbn              | VARCHAR(20)   | UNIQUE, NOT NULL                       |
| anio_publicacion  | INT           | NULL permitido                         |
| num_paginas       | INT           | NULL permitido                         |
| editorial         | VARCHAR(100)  | NULL permitido                         |
| id_autor          | INT           | FK → AUTOR(id_autor), NOT NULL         |
| id_categoria      | INT           | FK → CATEGORIA(id_categoria), NOT NULL |
| disponible        | BOOLEAN       | NOT NULL, DEFAULT TRUE                 |

---

### 4. AMIGO

Almacena la información de las personas a las que se prestan libros.

```
AMIGO (id_amigo, nombre, apellidos, email, telefono)
   PK: id_amigo
```

| Atributo      | Tipo de dato  | Restricciones                |
|---------------|---------------|------------------------------|
| id_amigo      | INT           | PK, AUTO_INCREMENT, NOT NULL |
| nombre        | VARCHAR(50)   | NOT NULL                     |
| apellidos     | VARCHAR(100)  | NOT NULL                     |
| email         | VARCHAR(100)  | UNIQUE, NULL permitido       |
| telefono      | VARCHAR(15)   | NULL permitido               |

---

### 5. PRESTAMO

Tabla intermedia que registra cada préstamo realizado. Resuelve la relación N:M entre LIBRO y AMIGO, aportando además atributos propios del préstamo (fechas y notas).

```
PRESTAMO (id_prestamo, id_libro, id_amigo, fecha_prestamo, fecha_devolucion_prevista, fecha_devolucion_real, notas)
   PK: id_prestamo
   FK: id_libro → LIBRO(id_libro)
   FK: id_amigo → AMIGO(id_amigo)
```

| Atributo                      | Tipo de dato  | Restricciones                       |
|-------------------------------|---------------|-------------------------------------|
| id_prestamo                   | INT           | PK, AUTO_INCREMENT, NOT NULL        |
| id_libro                      | INT           | FK → LIBRO(id_libro), NOT NULL      |
| id_amigo                      | INT           | FK → AMIGO(id_amigo), NOT NULL      |
| fecha_prestamo                | DATE          | NOT NULL                            |
| fecha_devolucion_prevista     | DATE          | NOT NULL                            |
| fecha_devolucion_real         | DATE          | NULL permitido (NULL = no devuelto) |
| notas                         | VARCHAR(255)  | NULL permitido                      |

---

## Restricciones de integridad

### Integridad de entidad
Toda tabla tiene una clave primaria definida que garantiza la unicidad de cada registro. Ningún campo declarado como PK puede ser nulo.

### Integridad referencial
Las claves foráneas garantizan la coherencia entre tablas relacionadas:

- `LIBRO.id_autor` debe existir previamente en `AUTOR.id_autor`.
- `LIBRO.id_categoria` debe existir previamente en `CATEGORIA.id_categoria`.
- `PRESTAMO.id_libro` debe existir previamente en `LIBRO.id_libro`.
- `PRESTAMO.id_amigo` debe existir previamente en `AMIGO.id_amigo`.

### Restricciones de dominio
- `LIBRO.isbn` debe ser único en toda la tabla (no puede haber dos libros con el mismo ISBN).
- `CATEGORIA.nombre` debe ser único (no puede haber dos categorías con el mismo nombre).
- `AMIGO.email` debe ser único cuando esté informado.
- `LIBRO.disponible` solo admite los valores TRUE o FALSE.

### Reglas de negocio
- Cuando se crea un nuevo registro en `PRESTAMO`, el atributo `LIBRO.disponible` del libro asociado debería actualizarse a FALSE.
- Cuando se actualiza `PRESTAMO.fecha_devolucion_real` con una fecha (es decir, se devuelve el libro), `LIBRO.disponible` debería volver a TRUE.

Estas reglas se gestionarán desde la capa de aplicación Java (módulo de Programación) mediante transacciones JDBC.

---

## Resumen del modelo

El modelo relacional resultante consta de **5 tablas** y **4 relaciones implementadas mediante claves foráneas**:

| Relación                   | Cardinalidad | Implementación              |
|----------------------------|--------------|-----------------------------|
| AUTOR escribe LIBRO        | 1:N          | FK en LIBRO.id_autor        |
| CATEGORIA agrupa LIBRO     | 1:N          | FK en LIBRO.id_categoria    |
| LIBRO se presta en PRESTAMO| 1:N          | FK en PRESTAMO.id_libro     |
| AMIGO recibe PRESTAMO      | 1:N          | FK en PRESTAMO.id_amigo     |

La relación N:M conceptual entre `LIBRO` y `AMIGO` se resuelve mediante la entidad asociativa `PRESTAMO`, que además incorpora información propia del préstamo (fechas y notas), siguiendo las buenas prácticas del modelo relacional.
