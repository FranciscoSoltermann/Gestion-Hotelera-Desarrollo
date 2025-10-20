package org.TPDesarrollo.UI;

import org.TPDesarrollo.DAOImp.HuespedDAOImp;
import org.TPDesarrollo.DAOImp.UsuarioDAOImp;
import org.TPDesarrollo.Excepciones.ContraseniaInvalida;
import org.TPDesarrollo.Excepciones.UsuarioNoEncontrado;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.TPDesarrollo.Gestores.GestorUsuario;
import org.TPDesarrollo.UI.acciones.AltaHuespedUI;
import org.TPDesarrollo.UI.acciones.BuscarHuespedUI;

import java.io.Console;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private final GestorHuesped gestorHuesped = new GestorHuesped(new HuespedDAOImp());
    private final GestorUsuario gestorUsuario = new GestorUsuario(new UsuarioDAOImp());

    public void iniciar() {
        if (autenticar()) {
            System.out.println("\nCargando menú principal del sistema...");
            mostrarMenuPrincipal();
        }
        scanner.close();
    }

    private boolean autenticar() {
        System.out.println("=== BIENVENIDO AL SISTEMA DE GESTIÓN HOTELERA ===");
        Console console = System.console();
        while (true) {
            try {
                System.out.print("Usuario: ");
                String nombre = scanner.nextLine();
                String contrasenia;

                if (console != null) {
                    contrasenia = new String(console.readPassword("Contraseña: "));
                } else {
                    System.out.print("Contraseña (visible): ");
                    contrasenia = scanner.nextLine();
                }

                if (gestorUsuario.autenticarUsuario(nombre, contrasenia)) {
                    System.out.println("\n✅ ¡Autenticación exitosa! Bienvenido/a, " + nombre + ".");
                    return true;
                }
            } catch (UsuarioNoEncontrado | ContraseniaInvalida e) {
                System.err.println("\n❌ Error: " + e.getMessage() + " Intente de nuevo.\n");
            }
        }
    }

    private void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Buscar Huésped");
            System.out.println("2. Dar Alta Huésped");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                AccionMenu accion = null;

                switch (opcion) {
                    case 1:
                        accion = new BuscarHuespedUI(scanner, gestorHuesped);
                        break;
                    case 2:
                        accion = new AltaHuespedUI(scanner, gestorHuesped);
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }

                if (accion != null) {
                    accion.ejecutar();
                }

            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
                opcion = -1; // Para que el bucle continúe
            }
        } while (opcion != 0);
    }
}