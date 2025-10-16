package org.TPDesarrollo.Gestores;

import org.TPDesarrollo.DAOS.UsuarioDAO;
import org.TPDesarrollo.DTOs.UsuarioDTO;
import org.TPDesarrollo.Excepciones.UsuarioNoEncontrado;
import org.TPDesarrollo.Excepciones.ContraseniaInvalida;

public class GestorUsuario {

    private final UsuarioDAO usuarioDAO;

    public GestorUsuario(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }


    public void verificarExistenciaUsuario(String nombre) throws UsuarioNoEncontrado {
        if (usuarioDAO.obtenerUsuarioPorNombre(nombre) == null) {
            throw new UsuarioNoEncontrado("El usuario '" + nombre + "' no existe.");
        }
    }

    public boolean autenticarUsuario(String nombre, String contrasenia)
            throws UsuarioNoEncontrado, ContraseniaInvalida {

        UsuarioDTO usuarioAlmacenado = usuarioDAO.obtenerUsuarioPorNombre(nombre);

        if (usuarioAlmacenado == null) {
            throw new UsuarioNoEncontrado("Usuario no encontrado: " + nombre);
        }

        if (!usuarioAlmacenado.getContrasenia().equals(contrasenia)) {
            throw new ContraseniaInvalida("Contraseña incorrecta.");
        }

        return true;
    }
}