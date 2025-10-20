package org.TPDesarrollo.UI.acciones;

import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.TPDesarrollo.UI.AccionMenu;

import java.util.Scanner;

public class BajaHuespedUI implements AccionMenu {

    private final Scanner scanner;
    private final GestorHuesped gestorHuesped;
    private final HuespedDTO huesped;

    public BajaHuespedUI(Scanner scanner, GestorHuesped gestorHuesped, HuespedDTO huesped) {
        this.scanner = scanner;
        this.gestorHuesped = gestorHuesped;
        this.huesped = huesped;
    }

    @Override
    public void ejecutar() {
        System.out.println("\n--- CASO DE USO 11: DAR DE BAJA HUÉSPED ---");
        System.out.println("ATENCIÓN: Esta acción es permanente y no se puede deshacer.");
        System.out.println("¿Está seguro que desea dar de baja a: " + huesped.getNombre() + " " + huesped.getApellido() + " (ID: " + huesped.getId() + ")?");
        System.out.print("Para confirmar, escriba 'SI' en mayúsculas: ");

        String confirmacion = scanner.nextLine().trim();

        if (confirmacion.equals("SI")) {
            try {
                gestorHuesped.darDeBajaHuesped(huesped.getId());
                System.out.println("✅ Huésped dado de baja con éxito.");
            } catch (Exception e) {
                System.err.println("❌ Ocurrió un error al dar de baja al huésped: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Operación de baja cancelada.");
        }
    }
}