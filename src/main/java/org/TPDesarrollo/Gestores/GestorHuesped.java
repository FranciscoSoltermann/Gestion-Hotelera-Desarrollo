package org.TPDesarrollo.Gestores;

import org.TPDesarrollo.Clases.Direccion;
import org.TPDesarrollo.Clases.Huesped;
import org.TPDesarrollo.DAOS.HuespedDAO;
import org.TPDesarrollo.DTOs.DireccionDTO;
import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Excepciones.CuitExistente;

import java.util.List;
import java.util.stream.Collectors;

public class GestorHuesped {
    private final HuespedDAO huespedDAO;

    public GestorHuesped(HuespedDAO huespedDAO) {

        this.huespedDAO = huespedDAO;
    }

    public List<HuespedDTO> buscarHuespedes(String apellido, String nombre, String tipoDocumento, Integer documento) {
        System.out.println("GESTOR: Solicitud para buscar huéspedes...");

        List<Huesped> huespedesEncontrados = huespedDAO.buscarHuespedes(apellido, nombre, tipoDocumento, documento);

        return huespedesEncontrados.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    public HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped) {
        Huesped huespedEntidad = huespedDAO.obtenerHuespedPorId(idHuesped);
        if (huespedEntidad != null) {
            return convertirA_DTO(huespedEntidad);
        }
        return null;
    }

    public void darDeAltaHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para dar de alta a " + huespedDTO.getNombre());
        String cuit = huespedDTO.getCuit();

        if (cuit == null || cuit.trim().isEmpty()) {
            final String POSICION_IVA_POR_DEFECTO = "Consumidor Final";

            if (huespedDTO.getPosicionIVA() == null || huespedDTO.getPosicionIVA().trim().isEmpty()) {
                huespedDTO.setPosicionIVA(POSICION_IVA_POR_DEFECTO);
            }
        } else {
            if (huespedDAO.existeHuespedConCuit(cuit)) {
                throw new CuitExistente(cuit);
            }
        }

        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        huespedDAO.darDeAltaHuesped(huespedEntidad);
    }

    public void modificarHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para modificar al huésped ID: " + huespedDTO.getId());

        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        huespedDAO.modificarHuesped(huespedEntidad);

        System.out.println("GESTOR: Modificación de " + huespedEntidad.getNombre() + " exitosa.");
    }

    public void darDeBajaHuesped(Integer id) {
        System.out.println("GESTOR: Solicitud para dar de baja al ID " + id);
        huespedDAO.darDeBajaHuesped(id);
    }

    private HuespedDTO convertirA_DTO(Huesped entidad) {
        HuespedDTO dto = new HuespedDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setCuit(entidad.getCuit());
        dto.setEmail(entidad.getEmail());

        dto.setTelefono(entidad.getTelefono());
        dto.setTipoDocumento(entidad.getTipoDocumento());
        dto.setDocumento(entidad.getDocumento());
        dto.setFechaNacimiento(entidad.getFechaNacimiento());
        dto.setNacionalidad(entidad.getNacionalidad());
        dto.setOcupacion(entidad.getOcupacion());
        dto.setPosicionIVA(entidad.getPosicionIVA());

        if (entidad.getDireccion() != null) {
            dto.setDireccion(new DireccionDTO(entidad.getDireccion()));
        }
        return dto;
    }

    private Huesped convertirA_Entidad(HuespedDTO dto) {
        Huesped entidad = new Huesped();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setApellido(dto.getApellido());
        entidad.setTelefono(dto.getTelefono());
        entidad.setEmail(dto.getEmail());
        entidad.setNacionalidad(dto.getNacionalidad());
        entidad.setCuit(dto.getCuit());
        entidad.setOcupacion(dto.getOcupacion());
        entidad.setPosicionIVA(dto.getPosicionIVA());
        entidad.setDocumento(dto.getDocumento());
        entidad.setTipoDocumento(dto.getTipoDocumento());
        entidad.setFechaNacimiento(dto.getFechaNacimiento());

        if (dto.getDireccion() != null) {
            entidad.setDireccion(convertirDireccionA_Entidad(dto.getDireccion()));
        }

        return entidad;
    }

    private Direccion convertirDireccionA_Entidad(DireccionDTO dto) {
        return new Direccion(
                dto.getPais(),
                dto.getProvincia(),
                dto.getLocalidad(),
                dto.getCalle(),
                dto.getNumero(),
                dto.getDepartamento(),
                dto.getPiso(),
                dto.getCodigoPostal()
        );
    }
}
