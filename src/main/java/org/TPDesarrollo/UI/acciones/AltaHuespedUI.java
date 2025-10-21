package org.TPDesarrollo.UI.acciones;

import org.TPDesarrollo.DTOs.DireccionDTO;
import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Enums.RazonSocial;
import org.TPDesarrollo.Enums.TipoDocumento;
import org.TPDesarrollo.Excepciones.CuitExistente;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.TPDesarrollo.UI.AccionMenu;
import org.TPDesarrollo.UI.ConsolaUtils; // Asegúrate de que este import esté presente

import java.util.Arrays;
import java.util.Scanner;

/**
 * Clase que maneja la interfaz de usuario para dar de alta un nuevo huésped.
 * Implementa la interfaz AccionMenu.
 */
public class AltaHuespedUI implements AccionMenu {
    private final Scanner scanner;
    private final GestorHuesped gestorHuesped;
    // Constructor que recibe el Scanner y el GestorHuesped
    public AltaHuespedUI(Scanner scanner, GestorHuesped gestorHuesped) {
        this.scanner = scanner;
        this.gestorHuesped = gestorHuesped;
    }
    // Método principal para ejecutar la acción de dar de alta un huésped
    @Override
    public void ejecutar() {

        System.out.println("\n--- CASO DE USO 09: DAR ALTA HUÉSPED ---");
        System.out.println("Ingrese los datos del nuevo huésped. Los campos con (*) son obligatorios.");

        HuespedDTO nuevoHuesped = new HuespedDTO();
        DireccionDTO nuevaDireccion = new DireccionDTO();

        // Llenar datos usando la clase ConsolaUtils
        llenarDatosPersonales(nuevoHuesped);
        llenarDatosFiscales(nuevoHuesped);
        llenarDatosDireccion(nuevaDireccion);
        nuevoHuesped.setDireccion(nuevaDireccion);

        System.out.print("\nConfirme el alta del nuevo huésped (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            try {
                gestorHuesped.darDeAltaHuesped(nuevoHuesped);
                System.out.println("✅ Huésped dado de alta con éxito.");
            } catch (CuitExistente e) {
                System.err.println("❌ ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("❌ Error inesperado durante el alta: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Operación de alta cancelada.");
        }
    }

    // Métodos privados para llenar los datos del huésped
    private void llenarDatosPersonales(HuespedDTO huesped) {
        System.out.println("\n[DATOS PERSONALES]");
        // Llamamos a los métodos a través de ConsolaUtils
        huesped.setNombre(ConsolaUtils.leerStringLetras(" - Nombres (*)", "", false, scanner));
        huesped.setApellido(ConsolaUtils.leerStringLetras(" - Apellidos (*)", "", false, scanner));
        huesped.setTelefono(ConsolaUtils.leerString(" - Telefono (*)", "", false, scanner));
        huesped.setEmail(ConsolaUtils.leerString(" - Email", "", true, scanner));
        huesped.setNacionalidad(ConsolaUtils.leerStringLetras(" - Nacionalidad (*)", "", false, scanner));
        huesped.setFechaNacimiento(ConsolaUtils.leerLocalDate(" - Fecha Nacimiento (dd/MM/yyyy) (*)", null, scanner));
        huesped.setOcupacion(ConsolaUtils.leerString(" - Ocupación (*)", "", false, scanner));

        String tiposValidos = Arrays.toString(TipoDocumento.values());
        while (huesped.getTipoDocumento() == null) {
            String tipoDocStr = ConsolaUtils.leerString(" - Tipo Documento (*)" + tiposValidos, null, false, scanner);
            try {
                huesped.setTipoDocumento(TipoDocumento.valueOf(tipoDocStr.toUpperCase().trim()));
            } catch (IllegalArgumentException e) {
                System.err.println("El tipo de documento '" + tipoDocStr + "' no es válido.");
            }
        }
        huesped.setDocumento(ConsolaUtils.leerInteger(" - Número Documento (*)", null, scanner));
    }
    // Método para llenar los datos fiscales del huésped
    private void llenarDatosFiscales(HuespedDTO huesped) {
        System.out.println("\n[DATOS FISCALES]");
        String cuit = ConsolaUtils.leerString(" - CUIT", null, true, scanner);
        huesped.setCuit(cuit);

        if (cuit != null && !cuit.trim().isEmpty()) {
            String tiposValidosIVA = Arrays.toString(RazonSocial.values());
            while (huesped.getPosicionIVA() == null) {
                String razonSocialStr = ConsolaUtils.leerString(" - Posición IVA (*)" + tiposValidosIVA, null, false, scanner);
                try {
                    RazonSocial razon = RazonSocial.valueOf(razonSocialStr.trim());
                    huesped.setPosicionIVA(razon.name());
                } catch (IllegalArgumentException e) {
                    System.err.println("La Razón Social '" + razonSocialStr + "' no es válida.");
                }
            }
        } else {
            huesped.setPosicionIVA("Consumidor Final");
            System.out.println(" - Posición IVA: Consumidor Final (por defecto).");
        }
    }
    // Método para llenar los datos de la dirección del huésped
    private void llenarDatosDireccion(DireccionDTO direccion) {
        System.out.println("\n[DATOS DE DIRECCIÓN]");
        direccion.setPais(ConsolaUtils.leerStringLetras(" - País (*)", "", false, scanner));
        direccion.setProvincia(ConsolaUtils.leerStringLetras(" - Provincia (*)", "", false, scanner));
        direccion.setLocalidad(ConsolaUtils.leerStringLetras(" - Localidad (*)", "", false, scanner));
        direccion.setCalle(ConsolaUtils.leerString(" - Calle (*)", "", false, scanner));
        direccion.setNumero(ConsolaUtils.leerInteger(" - Número (*)", null, scanner));
        direccion.setPiso(ConsolaUtils.leerString(" - Piso", "", true, scanner));
        direccion.setDepartamento(ConsolaUtils.leerString(" - Departamento", "", true, scanner));
        direccion.setCodigoPostal(ConsolaUtils.leerString(" - Código Postal (*)", "", false, scanner));
    }
}