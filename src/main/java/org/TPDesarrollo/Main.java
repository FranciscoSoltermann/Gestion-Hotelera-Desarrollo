package org.TPDesarrollo;

import org.TPDesarrollo.DAOImp.HuespedDAOImp;
import org.TPDesarrollo.DTOs.HuespedDTO;
import org.TPDesarrollo.DTOs.DireccionDTO;
import org.TPDesarrollo.Gestores.GestorHuesped;
import org.TPDesarrollo.Gestores.GestorUsuario;
import org.TPDesarrollo.DAOImp.UsuarioDAOImp;
import org.TPDesarrollo.Excepciones.ContraseniaInvalida;
import org.TPDesarrollo.Excepciones.UsuarioNoEncontrado;
import org.TPDesarrollo.Enums.TipoDocumento;

import java.io.Console;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Console console = System.console();
    private static final GestorHuesped gestorHuesped = new GestorHuesped(new HuespedDAOImp());
    private static final GestorUsuario gestorUsuario = new GestorUsuario(new UsuarioDAOImp());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        if (autenticar()) {
            System.out.println("\nCargando menú principal del sistema...");
            mostrarMenuPrincipal();
        }
        scanner.close();
    }

    private static boolean autenticar() {
        System.out.println("=== BIENVENIDO AL SISTEMA DE GESTIÓN HOTELERA ===");
        while (true) {
            try {
                System.out.print("Usuario: ");
                String nombre = scanner.nextLine();

                String contrasenia;
                if (console != null) {
                    char[] passwordArray = console.readPassword("Contraseña: ");
                    contrasenia = new String(passwordArray);
                } else {
                    System.out.print("Contraseña (visible en IDE): ");
                    contrasenia = scanner.nextLine();
                }

                boolean loginExitoso = gestorUsuario.autenticarUsuario(nombre, contrasenia);

                if (loginExitoso) {
                    System.out.println("\n✅ ¡Autenticación exitosa! Bienvenido/a, " + nombre + ".");
                    return true;
                }

            } catch (UsuarioNoEncontrado | ContraseniaInvalida e) {
                System.err.println("\n❌ Error: " + e.getMessage());
                System.out.println("Por favor, intente de nuevo.\n");
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        int opcion = -1;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Buscar Huésped (CU02)");
            System.out.println("2. Dar Alta Huésped (CU09)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        ejecutarCU02_BuscarHuesped();
                        break;
                    case 2:
                        ejecutarCU09_DarAltaHuesped();
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        } while (opcion != 0);
    }

    private static void ejecutarCU02_BuscarHuesped() {
        System.out.println("\n--- CASO DE USO 02: BUSCAR HUÉSPED ---");

        String apellido = "";
        String nombre = "";
        String tipoDoc = "";
        Integer documento = null;
        String entradaDocumento;

        System.out.println("Ingrese los criterios de búsqueda (deje en blanco para omitir):");

        System.out.print("Apellido: ");
        apellido = scanner.nextLine().trim();

        System.out.print("Nombres: ");
        nombre = scanner.nextLine().trim();

        System.out.print("Tipo Documento (DNI/PASAPORTE/LC/LE): ");
        tipoDoc = scanner.nextLine().trim();

        System.out.print("Número Documento: ");
        entradaDocumento = scanner.nextLine().trim();

        if (!entradaDocumento.isEmpty()) {
            try {
                documento = Integer.parseInt(entradaDocumento);
            } catch (NumberFormatException e) {
                System.err.println("Advertencia: El número de documento ingresado no es un número válido y será ignorado en la búsqueda.");
                documento = null;
            }
        }

        List<HuespedDTO> resultados = gestorHuesped.buscarHuespedes(apellido, nombre, tipoDoc.isEmpty() ? null : tipoDoc, documento);

        if (resultados.isEmpty()) {
            System.out.println("\n⚠️ No se encontraron huéspedes que coincidan con los criterios.");
            System.out.print("¿Desea dar de alta un nuevo huésped (S/N)? ");
            if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                // ref: iniciarCU11_DarAltaHuesped()
                System.out.println(">> Iniciando CU11: Dar de alta huésped...");
                // Aquí iría la llamada al CU11
            }
        } else {
            System.out.println("\n✅ Se encontraron " + resultados.size() + " huésped(es):");
            mostrarResultados(resultados);

            Integer idSeleccionado = null;
            while (idSeleccionado == null) {
                System.out.print("Ingrese el ID del huésped para seleccionar o '0' para CANCELAR: ");
                try {
                    idSeleccionado = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido. Por favor, ingrese un número.");
                    continue;
                }

                if (idSeleccionado == 0) {
                    System.out.println("Búsqueda cancelada.");
                    return;
                }

                final Integer finalId = idSeleccionado;
                HuespedDTO huespedSeleccionado = resultados.stream()
                        .filter(h -> h.getId().equals(finalId))
                        .findFirst()
                        .orElse(null);

                if (huespedSeleccionado != null) {
                    HuespedDTO dtoCompleto = gestorHuesped.obtenerHuespedSeleccionado(idSeleccionado);
                    mostrarMenuHuespedSeleccionado(dtoCompleto);
                    return;

                } else {
                    System.out.println("❌ ERROR: ID no válido o no encontrado en la lista de resultados.");
                    System.out.print("¿Desea dar de alta un nuevo huésped (S/N)? ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
                        // ref: iniciarCU11_DarAltaHuesped()
                        System.out.println(">> Iniciando CU11: Dar de alta huésped...");
                        // Aquí iría la llamada al CU11
                        return;
                    }
                    idSeleccionado = null;
                }
            }
        }
    }

    private static void ejecutarCU09_DarAltaHuesped() {
        System.out.println("\n--- CASO DE USO 09: DAR ALTA HUÉSPED ---");

        // **ESTA ES LA ZONA DONDE DEBES AGREGAR LA LÓGICA DE CAPTURA DE DATOS**
        // 1. Crear un nuevo HuespedDTO:
        HuespedDTO nuevoHuesped = new HuespedDTO();

        // 2. Pedir los datos al usuario (usando los métodos leerCampoString/Integer/LocalDate existentes o nuevos):
        System.out.println("Ingrese los datos del nuevo huésped:");
        nuevoHuesped.setNombre(leerCampoString("Nombre", "", false));
        nuevoHuesped.setApellido(leerCampoString("Apellido", "", false));
        // ... (resto de campos: telefono, email, documento, fechaNacimiento, etc.) ...

        // 3. Confirmar y llamar al gestor:
        System.out.println("\nConfirme el alta del nuevo huésped (S/N):");
        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            // Asegúrate de que el método darDeAltaHuesped reciba un HuespedDTO
            gestorHuesped.darDeAltaHuesped(nuevoHuesped);
            System.out.println("✅ Huésped dado de alta con éxito.");
        } else {
            System.out.println("❌ Operación de alta cancelada.");
        }
    }

    private static void ejecutarCU10_ModificarHuesped(HuespedDTO huespedOriginal) {
        System.out.println("\n--- CASO DE USO 10: MODIFICAR HUÉSPED ---");
        System.out.println("Modificando huésped ID: " + huespedOriginal.getId() + " - " + huespedOriginal.getApellido() + ", " + huespedOriginal.getNombre());
        System.out.println("Deje el campo vacío para mantener el valor actual.");

        HuespedDTO huespedModificado = clonarHuespedDTO(huespedOriginal);
        DireccionDTO direccionModificada = clonarDireccionDTO(huespedOriginal.getDireccion());

        System.out.println("\n[DATOS PERSONALES]");
        huespedModificado.setNombre(leerCampoString("Nombres", huespedOriginal.getNombre(), false));
        huespedModificado.setApellido(leerCampoString("Apellido", huespedOriginal.getApellido(), false));
        huespedModificado.setTelefono(leerCampoString("Teléfono", huespedOriginal.getTelefono(), true));
        huespedModificado.setEmail(leerCampoString("Email", huespedOriginal.getEmail(), true));
        huespedModificado.setNacionalidad(leerCampoString("Nacionalidad", huespedOriginal.getNacionalidad(), true));

        System.out.println("\n[DATOS DE IDENTIFICACIÓN]");

        String tiposValidos = Arrays.toString(TipoDocumento.values());
        String nuevoTipoDoc = leerCampoString("Tipo Documento (" + tiposValidos + ")", huespedOriginal.getTipoDocumento() != null ? huespedOriginal.getTipoDocumento().name() : "", false);
        if (!nuevoTipoDoc.isEmpty()) {
            try {
                huespedModificado.setTipoDocumento(TipoDocumento.valueOf(nuevoTipoDoc.toUpperCase().trim()));
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Tipo de documento inválido. Se mantiene el valor original.");
            }
        }
        huespedModificado.setDocumento(leerCampoInteger("Número Documento", huespedOriginal.getDocumento()));
        huespedModificado.setCuit(leerCampoString("CUIT", huespedOriginal.getCuit(), true));
        huespedModificado.setFechaNacimiento(leerCampoLocalDate("Fecha Nacimiento (dd/MM/yyyy)", huespedOriginal.getFechaNacimiento()));

        System.out.println("\n[DATOS LABORALES/FISCALES]");
        huespedModificado.setOcupacion(leerCampoString("Ocupación", huespedOriginal.getOcupacion(), true));
        huespedModificado.setPosicionIVA(leerCampoString("Posición IVA", huespedOriginal.getPosicionIVA(), true));

        if (direccionModificada != null) {
            System.out.println("\n[DATOS DE DIRECCIÓN]");
            direccionModificada.setPais(leerCampoString("País", direccionModificada.getPais(), true));
            direccionModificada.setProvincia(leerCampoString("Provincia", direccionModificada.getProvincia(), true));
            direccionModificada.setLocalidad(leerCampoString("Localidad", direccionModificada.getLocalidad(), true));
            direccionModificada.setCalle(leerCampoString("Calle", direccionModificada.getCalle(), true));
            direccionModificada.setNumero(leerCampoInteger("Número", direccionModificada.getNumero()));
            direccionModificada.setPiso(leerCampoString("Piso", direccionModificada.getPiso(), true));
            direccionModificada.setDepartamento(leerCampoString("Departamento", direccionModificada.getDepartamento(), true));
            direccionModificada.setCodigoPostal(leerCampoString("Código Postal", direccionModificada.getCodigoPostal(), true));
        }

        huespedModificado.setDireccion(direccionModificada);

        System.out.println("\n---------------------------------------------------");
        System.out.println("Confirme la modificación del huésped (S/N):");
        System.out.println("---------------------------------------------------");

        if (scanner.nextLine().trim().equalsIgnoreCase("S")) {
            gestorHuesped.modificarHuesped(huespedModificado);
            System.out.println("✅ Huésped modificado con éxito.");
        } else {
            System.out.println("❌ Modificación cancelada.");
        }
    }

    private static String leerCampoString(String label, String valorActual, boolean puedeSerVacio) {
        System.out.printf("%s [%s]: ", label, valorActual != null ? valorActual : "");
        String entrada = scanner.nextLine().trim();

        if (entrada.isEmpty()) {
            return valorActual;
        }

        return entrada;
    }

    private static Integer leerCampoInteger(String label, Integer valorActual) {
        String valorActualStr = valorActual != null ? String.valueOf(valorActual) : "";

        while (true) {
            System.out.printf("%s [%s]: ", label, valorActualStr);
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                return valorActual;
            }

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Valor no numérico. Intente de nuevo o deje en blanco para mantener el original.");
            }
        }
    }

    private static LocalDate leerCampoLocalDate(String label, LocalDate valorActual) {
        String valorActualStr = valorActual != null ? valorActual.format(DATE_FORMATTER) : "";

        while (true) {
            System.out.printf("%s [%s]: ", label, valorActualStr);
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                return valorActual;
            }

            try {
                return LocalDate.parse(entrada, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.err.println("⚠️ Formato de fecha inválido. Use el formato dd/MM/yyyy o deje en blanco para mantener el original.");
            }
        }
    }

    private static HuespedDTO clonarHuespedDTO(HuespedDTO huesped) {
        HuespedDTO huespedAux = new HuespedDTO();
        huespedAux.setId(huesped.getId());
        huespedAux.setNombre(huesped.getNombre());
        huespedAux.setApellido(huesped.getApellido());
        huespedAux.setTelefono(huesped.getTelefono());
        huespedAux.setTipoDocumento(huesped.getTipoDocumento());
        huespedAux.setDocumento(huesped.getDocumento());
        huespedAux.setFechaNacimiento(huesped.getFechaNacimiento());
        huespedAux.setNacionalidad(huesped.getNacionalidad());
        huespedAux.setEmail(huesped.getEmail());
        huespedAux.setCuit(huesped.getCuit());
        huespedAux.setOcupacion(huesped.getOcupacion());
        huespedAux.setPosicionIVA(huesped.getPosicionIVA());

        if (huesped.getDireccion() != null) {
            huespedAux.setDireccion(clonarDireccionDTO(huesped.getDireccion()));
        }

        return huespedAux;
    }

    private static DireccionDTO clonarDireccionDTO(DireccionDTO direccion) {
        if (direccion == null) return null;
        return new DireccionDTO(
                direccion.getPais(),
                direccion.getProvincia(),
                direccion.getLocalidad(),
                direccion.getCalle(),
                direccion.getNumero(),
                direccion.getDepartamento(),
                direccion.getPiso(),
                direccion.getCodigoPostal()
        );
    }

    private static void mostrarMenuHuespedSeleccionado(HuespedDTO huesped) {
        int opcion = -1;

        System.out.println("\n--- HUÉSPED SELECCIONADO: " + huesped.getNombre() + " " + huesped.getApellido() + " (ID: " + huesped.getId() + ") ---");

        do {
            System.out.println("1. Modificar Huésped (CU10)");
            System.out.println("2. Dar de Baja Huésped (CU11)");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1:
                        ejecutarCU10_ModificarHuesped(huesped);
                        opcion = 0;
                        break;
                    case 2:
                        System.out.println(">> Iniciando CU11: Dar de Baja Huésped...");
                        ejecutarCU11_DarBajaHuesped(huesped.getId(), huesped.getNombre(), huesped.getApellido());
                        opcion = 0;
                        break;
                    case 0:
                        System.out.println("Volviendo al Menú Principal.");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        } while (opcion != 0);
    }

    private static void ejecutarCU11_DarBajaHuesped(Integer id, String nombre, String apellido) {
        System.out.println("\n--- CONFIRMAR BAJA DE HUÉSPED ---");
        System.out.println("¿Está seguro que desea dar de baja a: " + nombre + " " + apellido + " (ID: " + id + ")?");
        System.out.print("Confirme escribiendo 'SI': ");

        if (scanner.nextLine().trim().equalsIgnoreCase("SI")) {
            gestorHuesped.darDeBajaHuesped(id);
            System.out.println("✅ Huésped dado de baja con éxito.");
        } else {
            System.out.println("❌ Operación de baja cancelada.");
        }
    }

    private static void mostrarResultados(List<HuespedDTO> resultados) {
        System.out.println("\n" + "=".repeat(500));
        System.out.printf("%-5s %-20s %-20s %-20s %-20s %-35s %-20s %-20s %-20s %-30s %-20s %-20s %-20s %-20s %-20s %-35s %-35s %-20s %-20s%n", "ID", "APELLIDO",
                "NOMBRE", "DOCUMENTO", "TIPO DOCUMENTO", "EMAIL", "PAIS", "PROVINCIA", "LOCALIDAD", "CALLE", "NUMERO", "DEPARTAMENTO", "PISO", "CODIGO POSTAL",
                "CUIT", "OCUPACION", "POSICION IVA", "FECHA NACIMIENTO", "NACIONALIDAD");
        System.out.println("------------------------------------------------------------------");
        for (HuespedDTO dto : resultados) {
            String pais = dto.getDireccion() != null ? dto.getDireccion().getPais() : "";
            String provincia = dto.getDireccion() != null ? dto.getDireccion().getProvincia() : "";
            String localidad = dto.getDireccion() != null ? dto.getDireccion().getLocalidad() : "";
            String calle = dto.getDireccion() != null ? dto.getDireccion().getCalle() : "";
            String numero = dto.getDireccion() != null ? String.valueOf(dto.getDireccion().getNumero()) : "";
            String departamento = dto.getDireccion() != null ? dto.getDireccion().getDepartamento() : "";
            String piso = dto.getDireccion() != null ? dto.getDireccion().getPiso() : "";
            String codigoPostal = dto.getDireccion() != null ? dto.getDireccion().getCodigoPostal() : "";
            String fechaNacimiento = dto.getFechaNacimiento() != null ? dto.getFechaNacimiento().format(DATE_FORMATTER) : "";
            System.out.printf("%-5s %-20s %-20s %-20s %-20s %-35s %-20s %-20s %-20s %-30s %-20s %-20s %-20s %-20s %-20s %-35s %-35s %-20s %-20s%n",
                    dto.getId(), dto.getApellido(), dto.getNombre(), dto.getDocumento(), dto.getTipoDocumento() != null ? dto.getTipoDocumento().name() : "",
                    dto.getEmail() != null ? dto.getEmail() : "", pais, provincia, localidad, calle, numero, departamento, piso, codigoPostal,
                    dto.getCuit() != null ? dto.getCuit() : "",
                    dto.getOcupacion() != null ? dto.getOcupacion() : "",
                    dto.getPosicionIVA() != null ? dto.getPosicionIVA() : "",
                    fechaNacimiento,
                    dto.getNacionalidad() != null ? dto.getNacionalidad() : "");
        }
        System.out.println("\n" + "=".repeat(500));
    }
}
