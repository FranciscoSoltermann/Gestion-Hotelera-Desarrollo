package org.TPDesarrollo.daos;

import org.TPDesarrollo.clases.Huesped;

public interface HuespedDAO {
    Huesped buscarHuesped(String apellido, String nombre, String tipoDocumento, Integer documento);
    void darDeAltaHuesped(Huesped huesped);
    void modificarHuesped(Huesped huesped);
    void darDeBajaHuesped(Integer id);
}
