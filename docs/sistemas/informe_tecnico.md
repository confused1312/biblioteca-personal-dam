# Informe Tecnico de Entorno de Ejecucion

**Proyecto**: Biblioteca Personal
**Modulo**: 0483 - Sistemas Informaticos
**Autor**: David Jamiro Lopez Wissroth
**Curso**: 1o DAM
**Fecha**: Mayo de 2026

---

## 1. Introduccion

Este informe describe el entorno de ejecucion de la aplicacion **Biblioteca Personal**, una aplicacion de escritorio desarrollada en Java que permite gestionar una coleccion personal de libros, los autores, las categorias literarias, los amigos a los que se prestan los libros y los prestamos realizados. La aplicacion utiliza una base de datos MySQL para la persistencia de los datos y se conecta a ella mediante JDBC.

El objetivo del documento es servir como guia tecnica completa para que cualquier persona pueda desplegar la aplicacion en un equipo nuevo, comprender los requisitos hardware y software, y realizar las tareas basicas de mantenimiento.

---

## 2. Descripcion del equipo de desarrollo

La aplicacion ha sido desarrollada y probada en un equipo con las siguientes caracteristicas reales:

| Componente              | Especificacion                              |
|-------------------------|---------------------------------------------|
| Fabricante              | Micro-Star International (MSI)              |
| Modelo                  | Pulse 16 AI C1VGKG                          |
| Procesador              | Intel Core Ultra 7 155H                     |
| Memoria RAM             | 32 GB                                       |
| Sistema operativo       | Microsoft Windows 11 Home                   |
| Version de Windows      | 10.0.26200 (Build 26200)                    |
| Arquitectura            | x64 (64 bits)                               |

Este equipo es muy superior a los requisitos minimos del proyecto, lo que permite tiempos de compilacion muy rapidos y ejecucion fluida de IntelliJ IDEA junto con MySQL Server.

---

## 3. Requisitos hardware

### 3.1 Requisitos minimos

Para ejecutar la aplicacion compilada (sin desarrollo) basta con un equipo modesto:

| Componente | Minimo                                |
|------------|---------------------------------------|
| Procesador | Intel Core i3 de 6a generacion o AMD Ryzen 3 |
| RAM        | 4 GB                                  |
| Disco      | 2 GB libres (Java + MySQL + datos)    |
| SO         | Windows 10 / Windows 11 / Linux moderno / macOS 11+ |

### 3.2 Requisitos recomendados (entorno de desarrollo)

Para un entorno de desarrollo comodo, donde ejecutar IntelliJ IDEA, MySQL Workbench y la aplicacion al mismo tiempo:

| Componente | Recomendado                           |
|------------|---------------------------------------|
| Procesador | Intel Core i5 / Ryzen 5 o superior    |
| RAM        | 8 GB minimo, 16 GB ideal              |
| Disco      | SSD con al menos 10 GB libres         |
| Pantalla   | Resolucion 1920x1080 o superior       |

---

## 4. Software necesario

A continuacion se listan todos los componentes software, con las versiones concretas que han sido utilizadas y validadas en este proyecto:

| Software                | Version utilizada     | Proposito                              |
|-------------------------|-----------------------|----------------------------------------|
| Java JDK                | 21.0.8 LTS (Temurin)  | Plataforma de ejecucion                |
| MySQL Server            | 8.0.46 Community      | Sistema gestor de base de datos        |
| MySQL Workbench         | 8.0.47                | Cliente grafico de MySQL               |
| MySQL Connector/J       | 8.4.0                 | Driver JDBC para conectar Java a MySQL |
| Apache Maven            | 3.9.x (integrado en IntelliJ) | Gestor de dependencias y build |
| IntelliJ IDEA           | 2026.1                | Entorno de desarrollo integrado        |
| Git                     | 2.53                  | Control de versiones                   |

Todos los componentes son gratuitos y de uso libre. Java se distribuye bajo la licencia GPL con excepciones, MySQL Community bajo GPL, IntelliJ IDEA Community bajo Apache 2.0 y Git bajo GPL v2.

---

## 5. Guia de instalacion paso a paso

Esta guia describe como preparar un equipo nuevo desde cero para ejecutar la aplicacion. Los pasos estan ordenados de forma que cada paso depende del anterior.

### 5.1 Instalacion de Java

1. Descargar el JDK 21 LTS desde [https://adoptium.net/](https://adoptium.net/) (Eclipse Temurin).
2. Ejecutar el instalador `.msi`.
3. Durante la instalacion, marcar la opcion **"Set JAVA_HOME variable"** y **"Add to PATH"**.
4. Reiniciar la terminal y verificar la instalacion:

   ```
   java -version
   ```

   Debe mostrar `openjdk version "21.0.x"`.

### 5.2 Instalacion de MySQL

1. Descargar el instalador desde [https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/).
2. Elegir el instalador grande (offline, ~600 MB).
3. En el asistente de instalacion, seleccionar **Setup Type: Full**.
4. Seguir el asistente con los valores por defecto excepto en:
   - **Authentication Method**: usar "Strong Password Encryption".
   - **MySQL Root Password**: definir una contrasena. En el desarrollo se ha utilizado `root1234` por simplicidad.
   - **Windows Service**: dejar activado el arranque automatico.
5. Tras la instalacion, anadir el binario al PATH:
   - Abrir **Variables de entorno del sistema** (sysdm.cpl).
   - Editar la variable `Path` y anadir la ruta `C:\Program Files\MySQL\MySQL Server 8.0\bin`.
6. Verificar la instalacion en una terminal nueva:

   ```
   mysql --version
   Get-Service MySQL80
   ```

   El servicio debe aparecer como `Running`.

### 5.3 Instalacion de IntelliJ IDEA

1. Descargar IntelliJ IDEA Community desde [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/).
2. Ejecutar el instalador y seguir el asistente con valores por defecto.
3. En el primer arranque, configurar el JDK 21 desde **File > Project Structure > Project SDK**.

### 5.4 Instalacion de Git

1. Descargar desde [https://git-scm.com/download/win](https://git-scm.com/download/win).
2. Ejecutar el instalador con los valores por defecto.
3. Configurar la identidad del usuario:

   ```
   git config --global user.name "Nombre Apellidos"
   git config --global user.email "correo@ejemplo.com"
   ```

---

## 6. Despliegue de la base de datos

Una vez instalados los componentes anteriores, se procede a crear la base de datos del proyecto.

### 6.1 Clonar el repositorio

```
mkdir C:\proyectos
cd C:\proyectos
git clone https://github.com/confused1312/biblioteca-personal-dam.git
cd biblioteca-personal-dam
```

### 6.2 Crear la base de datos y cargar los datos de ejemplo

**Importante**: el cliente `mysql` debe ejecutarse con el flag `--default-character-set=utf8mb4` para evitar problemas con tildes y caracteres especiales en sistemas Windows con codificacion CP850 por defecto.

```
mysql --default-character-set=utf8mb4 -u root -p
```

Tras introducir la contrasena, ejecutar dentro del cliente:

```
SOURCE C:/proyectos/biblioteca-personal-dam/sql/01_crear_tablas.sql;
SOURCE C:/proyectos/biblioteca-personal-dam/sql/02_insertar_datos.sql;
```

El primer script crea la base de datos `biblioteca_personal` y las cinco tablas necesarias (autor, categoria, libro, amigo, prestamo). El segundo script inserta datos de ejemplo: 10 autores, 8 categorias, 15 libros, 6 amigos y 8 prestamos en distintos estados.

### 6.3 Verificar las consultas

Para comprobar que la base de datos esta lista, se puede ejecutar el tercer script con consultas variadas:

```
SOURCE C:/proyectos/biblioteca-personal-dam/sql/03_consultas.sql;
```

Este script ejecuta 15 consultas representativas (filtros, JOINs, agregados, subconsultas) que deben devolver resultados sin errores.

---

## 7. Ejecucion de la aplicacion

### 7.1 Abrir el proyecto en IntelliJ

1. Abrir IntelliJ IDEA.
2. Seleccionar **File > Open** y navegar hasta `C:\proyectos\biblioteca-personal-dam\biblioteca-app`.
3. IntelliJ detectara automaticamente el archivo `pom.xml` y descargara las dependencias de Maven (puede tardar 1-2 minutos la primera vez).

### 7.2 Configurar la conexion a la base de datos

La conexion esta definida en el archivo `src/main/java/com/biblioteca/util/ConexionBD.java`. Si se ha utilizado una contrasena distinta de `root1234`, modificar la constante `PASSWORD`.

### 7.3 Ejecutar la aplicacion

1. Abrir la clase `Main.java`.
2. Pulsar el icono ▶ junto al metodo `main` o el atajo **Shift+F10**.
3. La aplicacion mostrara el menu principal en consola.

### 7.4 Funcionalidades disponibles

El menu principal ofrece las siguientes opciones:

1. **Libros**: listar todos, listar disponibles, buscar por titulo, anadir, modificar y eliminar.
2. **Autores**: listar, anadir, modificar y eliminar.
3. **Categorias**: listar, anadir, modificar y eliminar.
4. **Amigos**: listar, anadir, modificar y eliminar.
5. **Prestamos**: listar todos, listar activos, listar vencidos, prestar libro y devolver libro.
6. **Exportar catalogo a XML**: genera el archivo `xml/catalogo.xml` con todos los libros y lo valida automaticamente contra `xml/catalogo.xsd`.

---

## 8. Usuarios y permisos

### 8.1 Usuario de la base de datos

La aplicacion utiliza el usuario `root` de MySQL para todas las operaciones. Esto es aceptable en un entorno de desarrollo personal pero **no se recomienda en produccion**.

Para un despliegue real seria recomendable crear un usuario especifico con permisos limitados:

```
CREATE USER 'biblioteca'@'localhost' IDENTIFIED BY 'una_contrasena_segura';
GRANT SELECT, INSERT, UPDATE, DELETE ON biblioteca_personal.* TO 'biblioteca'@'localhost';
FLUSH PRIVILEGES;
```

Despues, modificar `ConexionBD.java` para usar este nuevo usuario.

### 8.2 Permisos del sistema operativo

- La carpeta del proyecto (`C:\proyectos\biblioteca-personal-dam`) debe tener permisos de lectura y escritura para el usuario que ejecuta la aplicacion. La aplicacion escribe en la carpeta `xml/` al exportar el catalogo.
- El servicio MySQL80 se ejecuta con la cuenta del sistema (`Standard System Account`) configurada durante la instalacion.

---

## 9. Mantenimiento

### 9.1 Copia de seguridad de la base de datos

Para hacer un backup completo de la base de datos:

```
mysqldump -u root -p biblioteca_personal > backup_biblioteca.sql
```

Esto genera un archivo SQL con todas las tablas y datos. Para restaurar:

```
mysql -u root -p biblioteca_personal < backup_biblioteca.sql
```

Se recomienda hacer una copia de seguridad antes de cualquier cambio importante.

### 9.2 Actualizacion de dependencias

Las dependencias del proyecto Java (driver JDBC) se gestionan con Maven. Para actualizar a una version mas nueva del driver MySQL:

1. Editar `biblioteca-app/pom.xml` y cambiar la version de `mysql-connector-j`.
2. En IntelliJ, hacer clic derecho sobre `pom.xml` > **Maven > Reload Project**.

### 9.3 Limpieza del directorio de compilacion

Maven genera archivos compilados en `biblioteca-app/target/`. Esta carpeta se puede borrar con seguridad cuando se quiera; Maven la regenerara en la siguiente compilacion. Esta carpeta no se incluye en el repositorio (esta excluida en `.gitignore`).

### 9.4 Logs

La aplicacion no genera archivos de log persistentes. Los errores se imprimen por consola (`System.err`) durante la ejecucion. En caso de problemas, ejecutar la aplicacion desde IntelliJ y revisar la pestana **Run** para ver los mensajes.

---

## 10. Resolucion de problemas comunes

A lo largo del desarrollo se han encontrado y documentado los siguientes problemas, que pueden volver a aparecer en futuros despliegues:

### 10.1 El comando `mysql` no se reconoce

**Sintoma**: al ejecutar `mysql --version` en PowerShell, Windows responde que el comando no existe.

**Causa**: el ejecutable `mysql.exe` no esta en el PATH del sistema.

**Solucion**: anadir la ruta `C:\Program Files\MySQL\MySQL Server 8.0\bin` a la variable de entorno `Path` y reiniciar la terminal.

### 10.2 Caracteres mal codificados (tildes y enes rotas)

**Sintoma**: al exportar a XML o consultar datos, los caracteres con tilde o ene aparecen como simbolos extranios (`├▒`, `├í`, etc.).

**Causa**: PowerShell en Windows utiliza por defecto la pagina de codigos CP850, que no es compatible con UTF-8. Cuando se carga un script SQL en UTF-8 desde el cliente `mysql`, los datos se guardan corruptos en la base de datos.

**Solucion**: ejecutar el cliente `mysql` siempre con el flag `--default-character-set=utf8mb4`:

```
mysql --default-character-set=utf8mb4 -u root -p
```

Si los datos ya estan corruptos, hay que volver a ejecutar el script de creacion de tablas y el de insercion de datos con el flag correcto.

### 10.3 Error de compilacion `illegal character: '\ufeff'`

**Sintoma**: IntelliJ falla al compilar archivos `.java` con un error sobre un caracter invalido al inicio del fichero.

**Causa**: PowerShell por defecto guarda los archivos UTF-8 con un BOM (Byte Order Mark) que Java no acepta.

**Solucion**: al crear archivos Java desde PowerShell, usar siempre la codificacion UTF-8 sin BOM:

```
$utf8NoBom = New-Object System.Text.UTF8Encoding $false
[System.IO.File]::WriteAllText("ruta\archivo.java", $contenido, $utf8NoBom)
```

### 10.4 La aplicacion no encuentra el driver JDBC

**Sintoma**: error `No suitable driver found for jdbc:mysql://localhost...`

**Causa**: Maven no ha descargado correctamente la dependencia `mysql-connector-j`.

**Solucion**: en IntelliJ, hacer clic derecho sobre `pom.xml` y elegir **Maven > Reload Project**. Verificar que existe la entrada en **External Libraries** del panel del proyecto.

---

## 11. Conclusion

La aplicacion Biblioteca Personal se ha desarrollado y probado satisfactoriamente en el entorno descrito. El conjunto de tecnologias utilizadas (Java 21 + MySQL 8 + JDBC + Maven) es estandar en la industria del desarrollo de software, lo que facilita su despliegue en cualquier equipo que cumpla con los requisitos minimos indicados.

La estructura del proyecto sigue buenas practicas de organizacion (separacion en capas modelo / DAO / utilidades / menu), control de versiones con Git, y documentacion en repositorio publico de GitHub, lo que permite que cualquier persona pueda clonar, instalar y ejecutar la aplicacion siguiendo este informe.
