package org.TPDesarrollo.UI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Clase de utilidades para manejar la entrada de datos por consola.
 * Proporciona métodos para leer diferentes tipos de datos con validación.
 */
public class ConsolaUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Evitar instanciación
    public static String leerString(String label, String valorActual, boolean esOpcional, Scanner scanner) {
        while (true) {
            System.out.printf("%s [%s]: ", label, valorActual != null && !valorActual.isEmpty() ? valorActual : "");
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            if (esOpcional || (valorActual != null && !valorActual.isEmpty())) {
                return valorActual;
            }
            System.out.println("❌ ERROR: Este campo es obligatorio.");
        }
    }
    // Método para leer una cadena que solo contenga letras y espacios
    public static String leerStringLetras(String label, String valorActual, boolean esOpcional, Scanner scanner) {
        while (true) {
            String entrada = leerString(label, valorActual, esOpcional, scanner);
            if (entrada.matches("^[\\p{L} .'-]+$")) { // Permite letras, espacios y algunos caracteres comunes en nombres
                return entrada;
            } else if (valorActual != null && entrada.equals(valorActual)){
                return valorActual;
            }
            System.out.println("❌ ERROR: El campo solo puede contener letras y espacios.");
        }
    }
    // Método para leer un Integer con validación
    public static Integer leerInteger(String label, Integer valorActual, Scanner scanner) {
        while (true) {
            System.out.printf("%s [%s]: ", label, valorActual != null ? String.valueOf(valorActual) : "");
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                if (valorActual != null) {
                    return valorActual;
                }
                System.out.println("❌ ERROR: Este campo es obligatorio.");
            } else {
                try {
                    return Integer.parseInt(entrada);
                } catch (NumberFormatException e) {
                    System.out.println("❌ ERROR: Formato de número inválido. Ingrese solo dígitos.");
                }
            }
        }
    }
    // Método para leer una fecha LocalDate con validación
    public static LocalDate leerLocalDate(String label, LocalDate valorActual, Scanner scanner) {
        while (true) {
            String valorMostrado = valorActual != null ? valorActual.format(DATE_FORMATTER) : "dd/MM/yyyy";
            System.out.printf("%s [%s]: ", label, valorMostrado);
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) {
                if (valorActual != null) {
                    return valorActual;
                }
                System.out.println("❌ ERROR: Este campo es obligatorio.");
            } else {
                try {
                    return LocalDate.parse(entrada, DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    System.out.println("❌ ERROR: Formato de fecha inválido. Utilice el formato dd/MM/yyyy.");
                }
            }
        }
    }
}