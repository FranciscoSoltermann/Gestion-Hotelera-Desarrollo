package org.TPDesarrollo.DAOS;

import org.TPDesarrollo.Clases.Huesped;
import java.util.List;

/**
 * Interfaz que define las operaciones Crear, Leer, Actualizar y Borrar para la entidad Huesped.
 */

public interface HuespedDAO {
    // Crear, Leer, Actualizar, Borrar
    List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDoc, Integer documento);
    Huesped obtenerHuespedPorId(Integer id);
    void darDeAltaHuesped(Huesped huesped);
    void modificarHuesped(Huesped huesped);
    void darDeBajaHuesped(Integer id);
    boolean existeHuespedConCuit(String cuit);
}
