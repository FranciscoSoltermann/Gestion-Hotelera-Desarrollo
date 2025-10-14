package org.TPDesarrollo;

import org.TPDesarrollo.gestores.GestorUsuario;
import org.TPDesarrollo.daos.UsuarioDAO;
import org.TPDesarrollo.daoImps.UsuarioDAOImp;
import org.TPDesarrollo.excepciones.ContraseniaInvalida;
import org.TPDesarrollo.excepciones.UsuarioNoEncontrado;
import java.io.Console; // Importante para la contraseña
import java.util.Scanner;

/**
 * Clase principal que inicia la aplicación y demuestra el funcionamiento
 * del Caso de Uso 1: Autenticar Usuario.
 */
public class Main {

    public static void main(String[] args) {
        // --- 1. CONFIGURACIÓN INICIAL ---
        // Se crean las clases necesarias, inyectando las dependencias.
        UsuarioDAO miDao = new UsuarioDAOImp();
        GestorUsuario miGestor = new GestorUsuario(miDao);
        Scanner scanner = new Scanner(System.in);
        // Objeto para leer la contraseña de forma segura desde la terminal.
        Console console = System.console();

        System.out.println("=== BIENVENIDO AL SISTEMA DE GESTIÓN HOTELERA ===");

        // Bucle para permitir reintentos de inicio de sesión.
        while (true) {
            try {
                // --- PASO 1: PEDIR Y VALIDAR EL NOMBRE DE USUARIO ---
                System.out.print("Usuario: ");
                String nombre = scanner.nextLine();
                // Se verifica si el usuario existe. Si no, lanza una excepción.
                miGestor.verificarExistenciaUsuario(nombre);

                // --- PASO 2: PEDIR LA CONTRASEÑA DE FORMA SEGURA ---
                String contrasenia;
                if (console != null) {
                    // MODO TERMINAL: Oculta la entrada del teclado. No se verán asteriscos,
                    // pero la contraseña no será visible, cumpliendo el requisito de seguridad.
                    char[] passwordArray = console.readPassword("Contraseña: ");
                    contrasenia = new String(passwordArray);
                } else {
                    // MODO IDE: Si la consola no está disponible, se pide de forma normal.
                    System.out.print("Contraseña (visible en IDE): ");
                    contrasenia = scanner.nextLine();
                }

                // --- PASO 3: INTENTAR LA AUTENTICACIÓN ---
                // Si el usuario existe, ahora se valida la contraseña.
                boolean loginExitoso = miGestor.autenticarUsuario(nombre, contrasenia);

                if (loginExitoso) {
                    System.out.println("\n✅ ¡Autenticación exitosa! Bienvenido/a, " + nombre + ".");
                    break; // Salir del bucle si el login es correcto.
                }

            } catch (UsuarioNoEncontrado | ContraseniaInvalida e) {
                // --- MANEJO DE ERRORES ---
                // Captura las excepciones y muestra el mensaje de error correspondiente.
                System.err.println("\n❌ Error: " + e.getMessage());
                System.out.println("Por favor, intente de nuevo.\n");
            }
        }

        System.out.println("\nCargando menú principal del sistema...");
        // Aquí continuaría la lógica para los otros casos de uso.
        scanner.close();
    }
}