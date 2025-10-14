package org.TPDesarrollo.daos;

import org.TPDesarrollo.dtos.UsuarioDTO;

public interface UsuarioDAO {
    UsuarioDTO obtenerUsuarioPorNombre(String nombre);
}
