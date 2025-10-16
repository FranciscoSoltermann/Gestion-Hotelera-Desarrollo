package org.TPDesarrollo.Excepciones;

public class TipoDocumentoInvalido extends RuntimeException {

    public TipoDocumentoInvalido(String mensaje) {
        super(mensaje);
    }

    public TipoDocumentoInvalido(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}