package org.TPDesarrollo.DAOS;

import org.TPDesarrollo.DTOs.UsuarioDTO;

/**
 * Interfaz para el acceso a datos de usuarios.
 */
public interface UsuarioDAO {
    UsuarioDTO obtenerUsuarioPorNombre(String nombre);
}
