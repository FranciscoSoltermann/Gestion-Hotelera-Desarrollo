package org.TPDesarrollo.Gestores;

import org.TPDesarrollo.DAOS.UsuarioDAO;
import org.TPDesarrollo.DTOs.UsuarioDTO;
import org.TPDesarrollo.Excepciones.UsuarioNoEncontrado;
import org.TPDesarrollo.Excepciones.ContraseniaInvalida;

/**
 * Gestor para operaciones relacionadas con usuarios.
 * Incluye métodos para verificar la existencia de un usuario y autenticarlo.
 * Utiliza UsuarioDAO para interactuar con la capa de datos.
 */
public class GestorUsuario {

    private final UsuarioDAO usuarioDAO;
    // Constructor
    public GestorUsuario(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    // Método para verificar la existencia de un usuario por su nombre
    public void verificarExistenciaUsuario(String nombre) throws UsuarioNoEncontrado {
        if (usuarioDAO.obtenerUsuarioPorNombre(nombre) == null) {
            throw new UsuarioNoEncontrado("El usuario '" + nombre + "' no existe.");
        }
    }
    // Método para autenticar un usuario con nombre y contraseña
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