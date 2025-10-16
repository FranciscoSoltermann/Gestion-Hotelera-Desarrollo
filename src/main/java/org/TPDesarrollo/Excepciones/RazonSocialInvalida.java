package org.TPDesarrollo.Excepciones;

public class RazonSocialInvalida extends RuntimeException {

    public RazonSocialInvalida(String message) {

        super(message);
    }
    public RazonSocialInvalida(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}
