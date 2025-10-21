package org.TPDesarrollo.Excepciones;

/**
 * Excepción lanzada cuando la razón social proporcionada es inválida.
 * Puede ser utilizada para indicar que la razón social no cumple con los requisitos
 * de formato o contenido esperado.
 */
public class RazonSocialInvalida extends RuntimeException {
    // Constructor
    public RazonSocialInvalida(String message) {

        super(message);
    }
    // Constructor con causa
    public RazonSocialInvalida(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}
