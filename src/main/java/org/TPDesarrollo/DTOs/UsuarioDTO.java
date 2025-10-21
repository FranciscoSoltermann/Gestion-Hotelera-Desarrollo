package org.TPDesarrollo.DTOs;

/**
 * DTO para transferir datos de usuario.
 */
public class UsuarioDTO {
    // Atributos
    private String nombre;
    private String contrasenia;
    // Constructor
    public UsuarioDTO(String nombre, String contrasenia) {
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
