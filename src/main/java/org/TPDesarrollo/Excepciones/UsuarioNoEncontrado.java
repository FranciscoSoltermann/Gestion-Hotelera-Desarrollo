package org.TPDesarrollo.Excepciones;

/**
 * Excepción lanzada cuando un usuario no es encontrado en el sistema.
 * Puede ser utilizada para indicar que el usuario buscado no existe o no está registrado.
 */
public class UsuarioNoEncontrado extends Exception {
    // Constructor
    public UsuarioNoEncontrado(String message) {
        super(message);
    }
}
