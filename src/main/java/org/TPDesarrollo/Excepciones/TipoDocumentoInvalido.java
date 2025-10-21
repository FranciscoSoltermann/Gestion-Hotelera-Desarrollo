package org.TPDesarrollo.Excepciones;

/**
 * Excepción lanzada cuando se encuentra un tipo de documento inválido.
 * Puede ser utilizada para indicar que el tipo de documento proporcionado
 * no coincide con los tipos esperados definidos en el sistema.
 */
public class TipoDocumentoInvalido extends RuntimeException {
    // Constructores
    public TipoDocumentoInvalido(String mensaje) {
        super(mensaje);
    }
    // Constructor con causa
    public TipoDocumentoInvalido(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}