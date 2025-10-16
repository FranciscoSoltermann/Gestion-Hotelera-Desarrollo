package org.TPDesarrollo.DAOS;

import org.TPDesarrollo.Clases.Huesped;
import java.util.List;

public interface HuespedDAO {
    List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDoc, Integer documento);
    Huesped obtenerHuespedPorId(Integer id);
    void darDeAltaHuesped(Huesped huesped);
    void modificarHuesped(Huesped huesped);
    void darDeBajaHuesped(Integer id);
    boolean existeHuespedConCuit(String cuit);
}
