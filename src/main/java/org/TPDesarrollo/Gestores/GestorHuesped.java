package org.TPDesarrollo.Gestores;

import org.TPDesarrollo.Clases.Direccion;
import org.TPDesarrollo.Clases.Huesped;
import org.TPDesarrollo.DAOS.HuespedDAO;
import org.TPDesarrollo.DTOs.DireccionDTO;
import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Excepciones.CuitExistente;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Gestor para manejar operaciones relacionadas con huéspedes.
 */
public class GestorHuesped {
    // Atributo DAO para acceder a los datos de huéspedes
    private final HuespedDAO huespedDAO;
    // Constructor que recibe una instancia de HuespedDAO
    public GestorHuesped(HuespedDAO huespedDAO) {

        this.huespedDAO = huespedDAO;
    }
    // Método para buscar huéspedes según criterios dados
    public List<HuespedDTO> buscarHuespedes(String apellido, String nombre, String tipoDocumento, Integer documento) {
        System.out.println("GESTOR: Solicitud para buscar huéspedes...");

        List<Huesped> huespedesEncontrados = huespedDAO.buscarHuespedes(apellido, nombre, tipoDocumento, documento);
        // Convertir la lista de entidades Huesped a una lista de DTOs HuespedDTO
        return huespedesEncontrados.stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }
    // Método para obtener un huésped seleccionado por su ID
    public HuespedDTO obtenerHuespedSeleccionado(Integer idHuesped) {
        Huesped huespedEntidad = huespedDAO.obtenerHuespedPorId(idHuesped);
        // Convertir la entidad Huesped a DTO HuespedDTO si no es nula
        if (huespedEntidad != null) {
            return convertirA_DTO(huespedEntidad);
        }
        return null;
    }
    // Método para dar de alta a un nuevo huésped
    public void darDeAltaHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para dar de alta a " + huespedDTO.getNombre());
        String cuit = huespedDTO.getCuit();
        // Validar CUIT y posición IVA
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
        // Convertir DTO a entidad y llamar al DAO para dar de alta
        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        huespedDAO.darDeAltaHuesped(huespedEntidad);
    }
    // Método para modificar los datos de un huésped existente
    public void modificarHuesped(HuespedDTO huespedDTO) {
        System.out.println("GESTOR: Solicitud para modificar al huésped ID: " + huespedDTO.getId());
        // Convertir DTO a entidad y llamar al DAO para modificar
        Huesped huespedEntidad = convertirA_Entidad(huespedDTO);
        huespedDAO.modificarHuesped(huespedEntidad);
        System.out.println("GESTOR: Modificación de " + huespedEntidad.getNombre() + " exitosa.");
    }
    // Método para dar de baja a un huésped por su ID
    public void darDeBajaHuesped(Integer id) {
        // Llamar al DAO para dar de baja
        System.out.println("GESTOR: Solicitud para dar de baja al ID " + id);
        huespedDAO.darDeBajaHuesped(id);
    }
// Métodos privados para convertir entre entidades y DTOs
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
// Conversión de DTO a Entidad
    private Huesped convertirA_Entidad(HuespedDTO dto) {
        // Crear una nueva instancia de Huesped y mapear los campos desde el DTO
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
// Conversión de DireccionDTO a Direccion Entidad
    private Direccion convertirDireccionA_Entidad(DireccionDTO dto) {
        // Crear una nueva instancia de Direccion y mapear los campos desde el DTO
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
