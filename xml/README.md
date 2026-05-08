# Exportacion de catalogo XML

Esta carpeta contiene el modulo de exportacion de la biblioteca a formato XML, junto con su esquema de validacion XSD.

## Archivos

- **catalogo.xsd**: esquema XML que define la estructura, tipos de datos y restricciones validas para los catalogos exportados.
- **catalogo_ejemplo.xml**: archivo XML de muestra con varios libros, validado correctamente contra el XSD.
- **catalogo.xml**: archivo generado dinamicamente por la aplicacion al exportar la coleccion.
- **catalogo_invalido.xml**: archivo XML que rompe a proposito multiples reglas del XSD para demostrar que la validacion funciona correctamente.

## Estructura del XSD

El esquema define:

- Un elemento raiz `catalogo` que contiene una seccion `info` y una lista de `libros`.
- Un tipo complejo `infoType` con propietario, fecha de exportacion (xs:date) y total de libros (xs:nonNegativeInteger).
- Un tipo complejo `libroType` con titulo, ISBN, autor, categoria, editorial opcional, anio publicacion opcional, paginas opcionales y disponibilidad.
- Un atributo `id` obligatorio de tipo entero positivo en cada libro.

### Restricciones de los tipos

- `tituloType`: cadena entre 1 y 150 caracteres.
- `isbnType`: cadena que cumple el patron `[0-9\-]{10,20}` (digitos y guiones, longitud entre 10 y 20).
- `anioType`: entero entre 1000 y 2100.
- `numPaginas`: entero positivo (xs:positiveInteger).
- `disponible`: booleano.

## Validacion automatica desde la aplicacion

La opcion 6 del menu principal (`Exportar catalogo a XML`) realiza dos pasos:

1. Genera el archivo `catalogo.xml` a partir de los datos actuales de la base de datos.
2. Valida el archivo generado contra `catalogo.xsd` usando la API estandar de Java (`javax.xml.validation`).

El resultado de la validacion se muestra en consola.

## Demostracion del control de errores del XSD

El archivo `catalogo_invalido.xml` se ha creado deliberadamente con multiples errores para demostrar que el XSD valida realmente y no es decorativo. Los errores incluidos son:

1. **Titulo vacio**: viola la restriccion `minLength=1` de `tituloType`.
2. **ISBN con letras**: viola el patron `[0-9\-]{10,20}` de `isbnType`.
3. **Anio fuera de rango**: el valor `3500` viola `maxInclusive=2100` de `anioType`.
4. **Paginas negativas**: el valor `-50` viola el tipo `xs:positiveInteger`.
5. **Atributo `id` ausente**: el segundo libro no incluye el atributo obligatorio.
6. **Booleano invalido**: el valor `maybe` no es un `xs:boolean` valido.

Al validar este archivo contra el XSD, el validador detecta y reporta los errores. Esto se puede comprobar abriendo el archivo en IntelliJ IDEA, que marca cada infraccion automaticamente.

## Validacion manual

El XML tambien puede validarse manualmente usando herramientas como IntelliJ IDEA (que detecta automaticamente el XSD referenciado en el atributo `xsi:noNamespaceSchemaLocation`) o herramientas en linea de validacion XML/XSD.