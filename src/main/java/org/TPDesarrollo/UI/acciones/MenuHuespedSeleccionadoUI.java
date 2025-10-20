package org.TPDesarrollo.UI.acciones;

import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.TPDesarrollo.UI.AccionMenu;

import java.util.Scanner;

public class MenuHuespedSeleccionadoUI implements AccionMenu {

    private final Scanner scanner;
    private final GestorHuesped gestorHuesped;
    private final HuespedDTO huesped;

    public MenuHuespedSeleccionadoUI(Scanner scanner, GestorHuesped gestorHuesped, HuespedDTO huesped) {
        this.scanner = scanner;
        this.gestorHuesped = gestorHuesped;
        this.huesped = huesped;
    }

    @Override
    public void ejecutar() {
        int opcion;
        do {
            System.out.println("\n--- HUÉSPED SELECCIONADO: " + huesped.getNombre() + " " + huesped.getApellido() + " (ID: " + huesped.getId() + ") ---");
            System.out.println("1. Modificar Huésped (CU10)");
            System.out.println("2. Dar de Baja Huésped (CU11)");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                AccionMenu accion = null;

                switch (opcion) {
                    case 1:
                        // Llama a la nueva clase para modificar
                        accion = new ModificarHuespedUI(scanner, gestorHuesped, huesped);
                        break;
                    case 2:
                        // Llama a la nueva clase para dar de baja
                        accion = new BajaHuespedUI(scanner, gestorHuesped, huesped);
                        break;
                    case 0:
                        System.out.println("Volviendo al Menú Principal...");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }

                if (accion != null) {
                    accion.ejecutar();
                    opcion = 0; // Para salir del sub-menú después de ejecutar la acción
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                opcion = -1; // Para continuar el bucle
            }
        } while (opcion != 0);
    }
}