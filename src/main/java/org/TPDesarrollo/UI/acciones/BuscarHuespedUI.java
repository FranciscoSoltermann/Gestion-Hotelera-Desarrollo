package org.TPDesarrollo.UI.acciones;

import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.TPDesarrollo.UI.AccionMenu;
import org.TPDesarrollo.UI.HuespedUIUtils; // Asegúrate que este import esté

import java.util.List;
import java.util.Scanner;
import java.util.Optional;

public class BuscarHuespedUI implements AccionMenu {

    private final Scanner scanner;
    private final GestorHuesped gestorHuesped;

    public BuscarHuespedUI(Scanner scanner, GestorHuesped gestorHuesped) {
        this.scanner = scanner;
        this.gestorHuesped = gestorHuesped;
    }

    @Override
    public void ejecutar() {
        System.out.println("\n--- CASO DE USO 02: BUSCAR HUÉSPED ---");
        System.out.println("Ingrese los criterios de búsqueda (deje en blanco para omitir o '0' para cancelar):");

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();
        if ("0".equals(apellido)) return;

        System.out.print("Nombres: ");
        String nombre = scanner.nextLine().trim();
        if ("0".equals(nombre)) return; // Corregido: antes comprobaba 'apellido' de nuevo

        System.out.print("Tipo Documento (DNI/PASAPORTE/LC/LE): ");
        String tipoDoc = scanner.nextLine().trim();
        if ("0".equals(tipoDoc)) return; // Corregido

        System.out.print("Número Documento: ");
        String entradaDocumento = scanner.nextLine().trim();
        if ("0".equals(entradaDocumento)) return; // Corregido

        Integer documento = null;
        if (!entradaDocumento.isEmpty()) {
            try {
                documento = Integer.parseInt(entradaDocumento);
            } catch (NumberFormatException e) {
                System.err.println("Advertencia: El número de documento no es un número válido y será ignorado.");
            }
        }

        List<HuespedDTO> resultados = gestorHuesped.buscarHuespedes(apellido, nombre, tipoDoc.isEmpty() ? null : tipoDoc, documento);

        if (resultados.isEmpty()) {
            manejarBusquedaSinResultados();
        } else {
            manejarBusquedaConResultados(resultados);
        }
    }

    private void manejarBusquedaSinResultados() {
        System.out.println("\n⚠️ No se encontraron huéspedes que coincidan con los criterios.");
        System.out.print("¿Desea dar de alta un nuevo huésped (S/N)? ");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            // Llamada correcta: Crear una instancia de AltaHuespedUI y ejecutarla
            new AltaHuespedUI(scanner, gestorHuesped).ejecutar();
        }
    }

    private void manejarBusquedaConResultados(List<HuespedDTO> resultados) {
        System.out.println("\n✅ Se encontraron " + resultados.size() + " huésped(es):");
        // Llamada correcta: Usar el método estático de la clase de utilidades
        HuespedUIUtils.mostrarResultados(resultados);

        Integer idSeleccionado = null;
        while (true) {
            System.out.print("\nIngrese el ID del huésped para seleccionar o '0' para CANCELAR: ");
            try {
                idSeleccionado = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ ID inválido. Por favor, ingrese un número.");
                continue;
            }

            if (idSeleccionado == 0) {
                System.out.println("Búsqueda cancelada.");
                return; // Salir del método
            }

            // Buscamos si el ID ingresado está en la lista de resultados
            final Integer finalId = idSeleccionado;
            Optional<HuespedDTO> huespedEncontrado = resultados.stream()
                    .filter(h -> h.getId().equals(finalId))
                    .findFirst();

            if (huespedEncontrado.isPresent()) {
                // Si el ID es válido, obtenemos todos sus datos y mostramos el submenú
                HuespedDTO dtoCompleto = gestorHuesped.obtenerHuespedSeleccionado(idSeleccionado);
                // Llamada correcta: Crear una instancia del menú de selección y ejecutarla
                new MenuHuespedSeleccionadoUI(scanner, gestorHuesped, dtoCompleto).ejecutar();
                return; // Salimos después de la selección
            } else {
                System.out.println("❌ ERROR: ID no válido o no encontrado en la lista de resultados.");
            }
        }
    }
}