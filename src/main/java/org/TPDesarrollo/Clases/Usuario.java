package org.TPDesarrollo.Clases;

/**
 * Clase que representa un usuario del sistema.
 * Contiene atributos para el nombre y la contraseña del usuario.
 */
public class Usuario {
    // Atributos
    private String nombre;
    private String contrasenia;
    // Constructor
    public Usuario(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }
    // Getters
    public String getNombre() {
        return nombre;
    }
    public String getContrasenia() {
        return contrasenia;
    }

}
