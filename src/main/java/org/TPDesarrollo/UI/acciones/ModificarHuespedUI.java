package org.TPDesarrollo.UI.acciones;

import org.TPDesarrollo.DTOs.DireccionDTO;
import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Enums.TipoDocumento;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.TPDesarrollo.UI.AccionMenu;
import org.TPDesarrollo.UI.ConsolaUtils;
import org.TPDesarrollo.UI.HuespedUIUtils;

import java.util.Arrays;
import java.util.Scanner;

public class ModificarHuespedUI implements AccionMenu {

    private final Scanner scanner;
    private final GestorHuesped gestorHuesped;
    private final HuespedDTO huespedOriginal;

    public ModificarHuespedUI(Scanner scanner, GestorHuesped gestorHuesped, HuespedDTO huespedOriginal) {
        this.scanner = scanner;
        this.gestorHuesped = gestorHuesped;
        this.huespedOriginal = huespedOriginal;
    }

    @Override
    public void ejecutar() {
        System.out.println("\n--- CASO DE USO 10: MODIFICAR HUÉSPED ---");
        System.out.println("Modificando huésped ID: " + huespedOriginal.getId() + " - " + huespedOriginal.getApellido() + ", " + huespedOriginal.getNombre());
        System.out.println("Deje el campo vacío y presione Enter para mantener el valor actual.");

        // Clonamos el DTO para no modificar el original hasta la confirmación
        HuespedDTO huespedModificado = HuespedUIUtils.clonarHuespedDTO(huespedOriginal);
        DireccionDTO direccionModificada = huespedModificado.getDireccion();

        // Solicitar datos
        llenarDatosPersonales(huespedModificado);
        llenarDatosIdentificacion(huespedModificado);
        llenarDatosLaborales(huespedModificado);
        if (direccionModificada != null) {
            llenarDatosDireccion(direccionModificada);
        }

        // Confirmación final
        System.out.println("\n---------------------------------------------------");
        System.out.print("Confirme la modificación del huésped (S/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            try {
                gestorHuesped.modificarHuesped(huespedModificado);
                System.out.println("✅ Huésped modificado con éxito.");
            } catch (Exception e) {
                System.err.println("❌ Error al modificar el huésped: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Modificación cancelada.");
        }
    }

    private void llenarDatosPersonales(HuespedDTO huesped) {
        System.out.println("\n[DATOS PERSONALES]");
        huesped.setNombre(ConsolaUtils.leerStringLetras(" - Nombres", huesped.getNombre(), false, scanner));
        huesped.setApellido(ConsolaUtils.leerStringLetras(" - Apellido", huesped.getApellido(), false, scanner));
        huesped.setTelefono(ConsolaUtils.leerString(" - Teléfono", huesped.getTelefono(), false, scanner));
        huesped.setEmail(ConsolaUtils.leerString(" - Email", huesped.getEmail(), true, scanner));
        huesped.setNacionalidad(ConsolaUtils.leerStringLetras(" - Nacionalidad", huesped.getNacionalidad(), false, scanner));
    }

    private void llenarDatosIdentificacion(HuespedDTO huesped) {
        System.out.println("\n[DATOS DE IDENTIFICACIÓN]");
        huesped.setFechaNacimiento(ConsolaUtils.leerLocalDate(" - Fecha Nacimiento", huesped.getFechaNacimiento(), scanner));

        // Manejo del Enum TipoDocumento
        String tiposValidos = Arrays.toString(TipoDocumento.values());
        String tipoDocActual = huesped.getTipoDocumento() != null ? huesped.getTipoDocumento().name() : "";
        String nuevoTipoDocStr = ConsolaUtils.leerString(" - Tipo Documento " + tiposValidos, tipoDocActual, false, scanner);
        if (!nuevoTipoDocStr.equals(tipoDocActual)) {
            try {
                huesped.setTipoDocumento(TipoDocumento.valueOf(nuevoTipoDocStr.toUpperCase().trim()));
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Tipo de documento inválido. Se mantiene el valor original: " + tipoDocActual);
            }
        }

        huesped.setDocumento(ConsolaUtils.leerInteger(" - Número Documento", huesped.getDocumento(), scanner));
        huesped.setCuit(ConsolaUtils.leerString(" - CUIT", huesped.getCuit(), true, scanner));
    }

    private void llenarDatosLaborales(HuespedDTO huesped) {
        System.out.println("\n[DATOS LABORALES/FISCALES]");
        huesped.setOcupacion(ConsolaUtils.leerString(" - Ocupación", huesped.getOcupacion(), false, scanner));
        huesped.setPosicionIVA(ConsolaUtils.leerString(" - Posición IVA", huesped.getPosicionIVA(), true, scanner));
    }

    private void llenarDatosDireccion(DireccionDTO direccion) {
        System.out.println("\n[DATOS DE DIRECCIÓN]");
        direccion.setPais(ConsolaUtils.leerStringLetras(" - País", direccion.getPais(), false, scanner));
        direccion.setProvincia(ConsolaUtils.leerStringLetras(" - Provincia", direccion.getProvincia(), false, scanner));
        direccion.setLocalidad(ConsolaUtils.leerStringLetras(" - Localidad", direccion.getLocalidad(), false, scanner));
        direccion.setCalle(ConsolaUtils.leerString(" - Calle", direccion.getCalle(), false, scanner));
        direccion.setNumero(ConsolaUtils.leerInteger(" - Número", direccion.getNumero(), scanner));
        direccion.setPiso(ConsolaUtils.leerString(" - Piso", direccion.getPiso(), true, scanner));
        direccion.setDepartamento(ConsolaUtils.leerString(" - Departamento", direccion.getDepartamento(), true, scanner));
        direccion.setCodigoPostal(ConsolaUtils.leerString(" - Código Postal", direccion.getCodigoPostal(), false, scanner));
    }
}