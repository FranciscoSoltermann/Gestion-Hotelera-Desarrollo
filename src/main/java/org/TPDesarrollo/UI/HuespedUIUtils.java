package org.TPDesarrollo.UI;

import org.TPDesarrollo.DTOs.DireccionDTO;
import org.TPDesarrollo.DTOs.HuespedDTO;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utilidades para la interfaz de usuario específica de Huéspedes.
 */
public class HuespedUIUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Método para mostrar los resultados de una búsqueda de huéspedes
    public static void mostrarResultados(List<HuespedDTO> resultados) {
        System.out.println("\n" + "=".repeat(100));
        System.out.printf("%-5s %-20s %-20s %-15s %-15s%n", "ID", "APELLIDO", "NOMBRE", "TIPO DOC", "NRO DOC");
        System.out.println("-".repeat(100));
        for (HuespedDTO dto : resultados) {
            System.out.printf("%-5s %-20s %-20s %-15s %-15s%n",
                    dto.getId(),
                    dto.getApellido(),
                    dto.getNombre(),
                    dto.getTipoDocumento() != null ? dto.getTipoDocumento().name() : "N/A",
                    dto.getDocumento());
        }
        System.out.println("=".repeat(100));
    }
    // Método para clonar un HuespedDTO
    public static HuespedDTO clonarHuespedDTO(HuespedDTO huesped) {
        HuespedDTO clon = new HuespedDTO();
        clon.setId(huesped.getId());
        clon.setNombre(huesped.getNombre());
        clon.setApellido(huesped.getApellido());
        clon.setTelefono(huesped.getTelefono());
        clon.setTipoDocumento(huesped.getTipoDocumento());
        clon.setDocumento(huesped.getDocumento());
        clon.setFechaNacimiento(huesped.getFechaNacimiento());
        clon.setNacionalidad(huesped.getNacionalidad());
        clon.setEmail(huesped.getEmail());
        clon.setCuit(huesped.getCuit());
        clon.setOcupacion(huesped.getOcupacion());
        clon.setPosicionIVA(huesped.getPosicionIVA());
        if (huesped.getDireccion() != null) {
            clon.setDireccion(clonarDireccionDTO(huesped.getDireccion()));
        }
        return clon;
    }
    // Método para clonar un DireccionDTO
    public static DireccionDTO clonarDireccionDTO(DireccionDTO direccion) {
        if (direccion == null) return null;
        return new DireccionDTO(
                direccion.getPais(), direccion.getProvincia(), direccion.getLocalidad(),
                direccion.getCalle(), direccion.getNumero(), direccion.getDepartamento(),
                direccion.getPiso(), direccion.getCodigoPostal()
        );
    }
}