package org.TPDesarrollo.DAOS;

import org.TPDesarrollo.DTOs.UsuarioDTO;

public interface UsuarioDAO {
    UsuarioDTO obtenerUsuarioPorNombre(String nombre);
}
