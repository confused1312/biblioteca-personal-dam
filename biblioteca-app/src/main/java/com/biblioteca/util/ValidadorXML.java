package com.biblioteca.util;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class ValidadorXML {

    public boolean validar(String rutaXml, String rutaXsd) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(rutaXsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(rutaXml)));
            return true;
        } catch (Exception e) {
            System.err.println("Error de validacion XML: " + e.getMessage());
            return false;
        }
    }
}