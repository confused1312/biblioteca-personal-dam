# Biblioteca Personal

Aplicacion de escritorio en Java para gestionar una coleccion personal de libros, los prestamos a amigos y la informacion asociada (autores, categorias, devoluciones). Los datos se almacenan en una base de datos MySQL y se acceden mediante JDBC.

Proyecto Intermodular del Ciclo Formativo de Grado Superior **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

---

## Indice

1. [Caracteristicas](#caracteristicas)
2. [Capturas](#capturas)
3. [Modulos cubiertos](#modulos-cubiertos)
4. [Estructura del repositorio](#estructura-del-repositorio)
5. [Modelo de datos](#modelo-de-datos)
6. [Requisitos](#requisitos)
7. [Instalacion](#instalacion)
8. [Uso](#uso)
9. [Tecnologias](#tecnologias)
10. [Autor](#autor)
11. [Licencia](#licencia)

---

## Caracteristicas

- Gestion CRUD completa de libros, autores, categorias y amigos.
- Registro de prestamos con fecha prevista de devolucion y deteccion automatica de prestamos vencidos.
- Control de disponibilidad: cuando un libro se presta queda marcado como no disponible hasta que se registra la devolucion.
- Busqueda de libros por titulo (filtros con LIKE).
- Exportacion del catalogo completo a formato XML con validacion automatica contra esquema XSD.
- Estructura por capas (modelo / DAO / utilidades / menu) que separa la logica de acceso a datos del resto.
- Manejo robusto de codificacion UTF-8 para tildes y caracteres especiales.

---

## Capturas

### Aplicacion en consola

Menu principal de la aplicacion con todas las opciones disponibles:

![Menu principal](docs/capturas/01_app_consola_menu.png)

Listado de los 15 libros del catalogo, con su estado de disponibilidad:

![Listado de libros](docs/capturas/02_app_listado_libros.png)

Listado de prestamos vencidos (libros prestados cuya fecha prevista de devolucion ya ha pasado):

![Prestamos vencidos](docs/capturas/03_app_prestamos_vencidos.png)

Exportacion del catalogo a XML y validacion contra el esquema XSD:

![Exportacion XML](docs/capturas/04_app_exportacion_xml.png)

### Base de datos

Tabla `libro` en MySQL Workbench con los 15 registros de ejemplo:

![MySQL Workbench](docs/capturas/05_mysql_workbench_libros.png)

### Repositorio

Vista del repositorio en GitHub:

![Repositorio GitHub](docs/capturas/06_github_repo.png)

---

## Modulos cubiertos

Este proyecto integra los seis modulos del primer curso de DAM:

| Codigo | Modulo                              | Aportacion del proyecto                                                                                       |
|--------|-------------------------------------|---------------------------------------------------------------------------------------------------------------|
| 0484   | Bases de Datos                      | Diseno E/R, modelo relacional, scripts SQL de creacion, insercion y consultas (15 consultas representativas). |
| 0487   | Entornos de Desarrollo              | Uso de IntelliJ IDEA, Maven, Git y GitHub con commits significativos a lo largo del desarrollo.               |
| 0373   | Lenguajes de Marcas                 | Exportacion del catalogo a XML, esquema XSD con tipos personalizados y validacion automatica.                 |
| 0485   | Programacion                        | Aplicacion Java orientada a objetos con conexion JDBC, manejo de excepciones y CRUD completo.                 |
| 0483   | Sistemas Informaticos               | Informe tecnico de entorno de ejecucion, instalacion paso a paso y mantenimiento.                             |
| MPO    | Ampliacion de Programacion          | Estructura por capas, validacion XML como mejora estructural respecto a un proyecto basico.                   |

---

## Estructura del repositorio

```
biblioteca-personal-dam/
+-- biblioteca-app/              -> Aplicacion Java (proyecto Maven)
|   +-- src/main/java/com/biblioteca/
|   |   +-- model/               -> Clases POJO (Autor, Libro, Categoria, Amigo, Prestamo)
|   |   +-- dao/                 -> Capa de acceso a datos (un DAO por entidad)
|   |   +-- util/                -> ConexionBD, ExportadorXML, ValidadorXML
|   |   +-- menu/                -> MenuPrincipal (interfaz por consola)
|   |   +-- Main.java            -> Punto de entrada
|   +-- pom.xml                  -> Dependencias Maven
+-- sql/
|   +-- 01_crear_tablas.sql      -> Creacion de la base de datos y las tablas
|   +-- 02_insertar_datos.sql    -> Datos de ejemplo
|   +-- 03_consultas.sql         -> 15 consultas representativas
+-- xml/
|   +-- catalogo.xsd             -> Esquema XSD con tipos y restricciones
|   +-- catalogo_ejemplo.xml     -> Ejemplo manual validado contra el XSD
|   +-- catalogo.xml             -> Salida generada por la aplicacion
|   +-- README.md                -> Documentacion del modulo XML
+-- docs/
|   +-- bbdd/                    -> Diagrama E/R y modelo relacional
|   +-- sistemas/                -> Informe tecnico de Sistemas Informaticos
|   +-- capturas/                -> Capturas de evidencia
+-- README.md                    -> Este documento
+-- LICENSE                      -> Licencia MIT
+-- .gitignore                   -> Exclusiones de Git
```

---

## Modelo de datos

La base de datos `biblioteca_personal` esta formada por cinco tablas:

- **autor**: catalogo de autores con nacionalidad y ano de nacimiento.
- **categoria**: generos literarios.
- **libro**: titulos, ISBN, editorial, ano de publicacion y disponibilidad. Tiene relaciones con `autor` y `categoria`.
- **amigo**: contactos a los que se prestan libros.
- **prestamo**: tabla intermedia que relaciona libros con amigos, registrando fechas previstas y reales de devolucion.

El diagrama entidad/relacion completo se encuentra en `docs/bbdd/diagrama_er_biblioteca.pdf` y el modelo relacional en `docs/bbdd/modelo_relacional.md`.

---

## Requisitos

- **Java JDK 21** o superior (probado con Eclipse Temurin 21.0.8 LTS).
- **MySQL Server 8.0** o superior.
- **Maven** (integrado en IntelliJ IDEA o instalado como standalone).
- **Git** (opcional, para clonar el repositorio).

Sistemas operativos compatibles: Windows 10/11, Linux moderno (probado en Ubuntu 22.04+) y macOS 11+.

Para detalles completos de requisitos hardware/software y guia de instalacion paso a paso consultar el [informe tecnico](docs/sistemas/informe_tecnico.md).

---

## Instalacion

### 1. Clonar el repositorio

```bash
git clone https://github.com/confused1312/biblioteca-personal-dam.git
cd biblioteca-personal-dam
```

### 2. Crear la base de datos

Importante: en Windows el cliente `mysql` debe ejecutarse con el flag `--default-character-set=utf8mb4` para evitar problemas con tildes y enes.

```bash
mysql --default-character-set=utf8mb4 -u root -p
```

Dentro del cliente:

```sql
SOURCE sql/01_crear_tablas.sql;
SOURCE sql/02_insertar_datos.sql;
```

### 3. Configurar la conexion

Si la contrasena de root no es `root1234`, editar el archivo:

```
biblioteca-app/src/main/java/com/biblioteca/util/ConexionBD.java
```

y modificar la constante `PASSWORD`.

### 4. Ejecutar

Abrir la carpeta `biblioteca-app` en IntelliJ IDEA, esperar a que Maven descargue las dependencias y ejecutar `Main.java`.

---

## Uso

Al arrancar, la aplicacion muestra el menu principal:

```
=== BIBLIOTECA PERSONAL ===
1. Libros
2. Autores
3. Categorias
4. Amigos
5. Prestamos
6. Exportar catalogo a XML
0. Salir
```

Cada opcion da acceso a un submenu con operaciones CRUD especificas. La opcion 6 genera el archivo `xml/catalogo.xml` con todos los libros y lo valida automaticamente contra el esquema `xml/catalogo.xsd`.

---

## Tecnologias

- **Java 21**: lenguaje de programacion principal.
- **MySQL 8.0**: sistema gestor de base de datos relacional.
- **JDBC** (driver mysql-connector-j 8.4.0): acceso a la base de datos desde Java.
- **Maven**: gestion de dependencias y compilacion.
- **javax.xml.validation**: API estandar de Java para validacion XML contra XSD.
- **IntelliJ IDEA**: entorno de desarrollo.
- **Git + GitHub**: control de versiones y alojamiento del repositorio.
- **draw.io**: diseno del diagrama entidad/relacion.

---

## Autor

**David Jamiro Lopez Wissroth**
Estudiante de 1o DAM
GitHub: [@confused1312](https://github.com/confused1312)

---

## Licencia

Este proyecto esta publicado bajo licencia MIT. Ver el archivo [LICENSE](LICENSE) para mas detalles.
