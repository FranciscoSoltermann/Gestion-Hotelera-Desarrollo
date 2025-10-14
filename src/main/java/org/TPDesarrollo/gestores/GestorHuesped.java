package org.TPDesarrollo.gestores;

import org.TPDesarrollo.clases.Direccion;
import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.daos.HuespedDAO;
import org.TPDesarrollo.dtos.DireccionDTO;
import org.TPDesarrollo.dtos.HuespedDTO;

public class GestorHuesped {
    private final HuespedDAO huespedDAO;

    public GestorHuesped(HuespedDAO huespedDAO) {
        this.huespedDAO = huespedDAO;
    }
    public HuespedDTO buscarHuesped(String apellido, String nombre, String tipoDocumento, Integer documento) {
        System.out.println("GESTOR: Solicitud para buscar a " + nombre + " " + apellido);
        Huesped huespedEntidad = huespedDAO.buscarHuesped(apellido, nombre, tipoDocumento, documento);
        if (huespedEntidad != null) {
            return convertirA_DTO(huespedEntidad);
        }
        return null;
    }

    public void darDeAltaHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para dar de alta a " + huespedDTO.getNombre());
        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        // Aquí podría ir lógica de negocio, ej: validar que el CUIT no exista
        huespedDAO.darDeAltaHuesped(huespedEntidad);
    }

    public void modificarHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para modificar a " + huespedDTO.getNombre());
        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        huespedDAO.modificarHuesped(huespedEntidad);
    }

    public void darDeBajaHuesped(Integer id) {
        System.out.println("GESTOR: Solicitud para dar de baja al ID " + id);
        huespedDAO.darDeBajaHuesped(id);
    }

    // --- Métodos de Conversión (privados) ---

    private HuespedDTO convertirA_DTO(Huesped entidad) {
        HuespedDTO dto = new HuespedDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setCuit(entidad.getCuit());
        dto.setEmail(entidad.getEmail());
        if (entidad.getDireccion() != null) {
            Direccion dirCompleta = entidad.getDireccion();
            dto.setDireccion(new DireccionDTO(dirCompleta));
        }
        return dto;
    }

    private Huesped convertirA_Entidad(HuespedDTO dto) {
        Huesped entidad = new Huesped();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setApellido(dto.getApellido());
        entidad.setCuit(dto.getCuit());
        entidad.setEmail(dto.getEmail());

        return entidad;
    }
}
