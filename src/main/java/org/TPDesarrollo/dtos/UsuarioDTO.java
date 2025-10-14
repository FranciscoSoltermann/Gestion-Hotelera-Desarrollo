package org.TPDesarrollo.dtos;

public class UsuarioDTO {
    private String nombre;
    private String contrasenia;
    public UsuarioDTO(String nombre, String contrasenia) {
        this.nombre = nombre;
        this.contrasenia = contrasenia;
    }
    public String getNombre() {
        return nombre;
    }
    public String getContrasenia() {
        return contrasenia;
    }

}
