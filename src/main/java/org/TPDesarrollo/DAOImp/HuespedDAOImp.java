package org.TPDesarrollo.DAOImp;

import org.TPDesarrollo.Clases.Huesped;
import org.TPDesarrollo.Clases.Direccion;
import org.TPDesarrollo.DAOS.HuespedDAO;
import org.TPDesarrollo.Enums.TipoDocumento;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Implementación de HuespedDAO que utiliza archivos CSV para almacenar y gestionar datos de huéspedes.
 */
public class HuespedDAOImp implements HuespedDAO {
    // Ruta del archivo CSV donde se almacenan los datos de los huéspedes
    private static final String RUTA_HUESPEDES = "src/main/resources/huespedes.csv";
    private final AtomicInteger contadorId;
    // Constructor
    public HuespedDAOImp() {
        // Inicializar el contador de ID basado en el último ID utilizado en el archivo CSV
        this.contadorId = new AtomicInteger(cargarUltimoId());
    }
    // --- MÉTODOS PÚBLICOS IMPLEMENTADOS ---
    @Override
    public void darDeAltaHuesped(Huesped huesped) {
        // Asegurar que el archivo existe y tiene encabezado
        asegurarArchivoExisteConEncabezado();
        huesped.setId(contadorId.incrementAndGet());
        // Agregar el nuevo huésped al archivo CSV
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_HUESPEDES, true))) {
            pw.println(huespedACSV(huesped));
            System.out.println("DAO: Huésped " + huesped.getApellido() + " guardado en CSV con ID: " + huesped.getId());
        } catch (IOException e) {
            System.err.println("DAO: Error al dar de alta huésped: " + e.getMessage());
        }
    }

    @Override
    public List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDocumento, Integer documento) {
        // Obtener todos los huéspedes
        List<Huesped> todosLosHuespedes = obtenerTodos();
    // Filtrar según los criterios proporcionados
        return todosLosHuespedes.stream()
                .filter(h -> (apellido == null || apellido.trim().isEmpty() ||
                        (h.getApellido() != null && h.getApellido().toLowerCase().startsWith(apellido.trim().toLowerCase()))) &&
                        (nombre == null || nombre.trim().isEmpty() ||
                                (h.getNombre() != null && h.getNombre().toLowerCase().startsWith(nombre.trim().toLowerCase()))) &&
                        (tipoDocumento == null || tipoDocumento.trim().isEmpty() ||
                                (h.getTipoDocumento() != null && h.getTipoDocumento().name().toLowerCase().startsWith(tipoDocumento.trim().toLowerCase()))) &&
                        (documento == null ||
                                (h.getDocumento() != null && h.getDocumento().toString().startsWith(documento.toString()))))
                .collect(Collectors.toList());
    }

    @Override
    public Huesped obtenerHuespedPorId(Integer id) {
        // Buscar el huésped por ID en la lista de todos los huéspedes
        return obtenerTodos().stream()
                .filter(h -> Objects.equals(h.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void modificarHuesped(Huesped huespedModificado) {
        // Leer todos los huéspedes del archivo
        List<Huesped> todosLosHuespedes = leerTodosLosHuespedesDelArchivo();
        boolean modificado = false;
        // Buscar y modificar el huésped correspondiente
        for (int i = 0; i < todosLosHuespedes.size(); i++) {
            if (Objects.equals(todosLosHuespedes.get(i).getId(), huespedModificado.getId())) {
                todosLosHuespedes.set(i, huespedModificado);
                modificado = true;
                break;
            }
        }
        // Si se modificó, reescribir el archivo completo
        if (modificado) {
            escribirArchivoCompleto(todosLosHuespedes);
            System.out.println("DAO: Huésped con ID " + huespedModificado.getId() + " modificado en CSV.");
        }
    }

    @Override
    public void darDeBajaHuesped(Integer id) {
        // Leer todos los huéspedes del archivo
        List<Huesped> todosLosHuespedes = leerTodosLosHuespedesDelArchivo();
        // Filtrar para eliminar el huésped con el ID especificado
        List<Huesped> huespedesRestantes = todosLosHuespedes.stream()
                .filter(h -> !Objects.equals(h.getId(), id))
                .collect(Collectors.toList());
        // Reescribir el archivo solo si se eliminó algún huésped
        if (huespedesRestantes.size() < todosLosHuespedes.size()) {
            escribirArchivoCompleto(huespedesRestantes);
            System.out.println("DAO: Huésped con ID " + id + " dado de baja del CSV.");
        }
    }

    public boolean existeHuespedConCuit(String cuit) {
        // Verificar si un huésped con el CUIT especificado ya existe en el archivo CSV
        if (cuit == null || cuit.trim().isEmpty()) {
            return false;
        }

        final String cuitBuscado = cuit.trim();
        final int CUIT_INDEX = 14;
        // Leer el archivo CSV y buscar el CUIT
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_HUESPEDES))) {
            String linea;
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                // Asegurarse de que el índice CUIT existe en la línea
                if (datos.length > CUIT_INDEX && !datos[CUIT_INDEX].trim().isEmpty()) {
                    String cuitExistente = datos[CUIT_INDEX].trim();

                    if (cuitBuscado.equalsIgnoreCase(cuitExistente)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("DAO: Error al verificar la existencia del CUIT en el archivo CSV: " + e.getMessage());
        }
        return false;
    }

    // --- MÉTODOS PRIVADOS AUXILIARES ---

    private List<Huesped> obtenerTodos() {
        // Leer todos los huéspedes del archivo CSV
        List<Huesped> huespedes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_HUESPEDES))) {
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    huespedes.add(mapearLineaA_Huesped(datos));
                }
            }
        } catch (IOException e) {
            System.err.println("DAO: Error al leer el archivo de huéspedes: " + e.getMessage());
        }
        return huespedes;
    }

    private Huesped mapearLineaA_Huesped(String[] datos) {
        // Mapear una línea del archivo CSV a un objeto Huesped
        Huesped h = new Huesped();
        try {
            h.setId(Integer.parseInt(datos[0].trim()));
            h.setNombre(datos[1].trim());
            h.setApellido(datos[2].trim());
            h.setTelefono(datos[3].trim());

            try {
                h.setDocumento(Integer.parseInt(datos[4].replaceAll("[^0-9]", "")));
            } catch (NumberFormatException e) {
                h.setDocumento(0);
            }

            try {

                h.setTipoDocumento(TipoDocumento.valueOf(datos[5].trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                h.setTipoDocumento(TipoDocumento.DNI);
            }

            if (datos.length > 6) h.setEmail(datos[6].trim());

            if (datos.length > 7) {
                Direccion direccion = new Direccion(
                        datos.length > 7 ? datos[7].trim() : "",        // Pais
                        datos.length > 8 ? datos[8].trim() : "",        // Provincia
                        datos.length > 9 ? datos[9].trim() : "",        // Localidad
                        datos.length > 10 ? datos[10].trim() : "",      // Calle
                        datos.length > 11 && !datos[11].trim().isEmpty() ? Integer.parseInt(datos[11].trim()) : 0,  // numero
                        datos.length > 12 ? datos[12].trim() : "",      // Departamento
                        datos.length > 13 ? datos[13].trim() : "",      // Piso
                        datos.length > 14 ? datos[14].trim() : ""       // CodigoPostal
                );
                h.setDireccion(direccion);
            }

            if (datos.length > 15) h.setCuit(datos[15].trim());
            if (datos.length > 16) h.setOcupacion(datos[16].trim());
            if (datos.length > 17) h.setPosicionIVA(datos[17].trim());

            if (datos.length > 18 && !datos[18].trim().isEmpty()) {
                try {
                    h.setFechaNacimiento(LocalDate.parse(datos[18].trim()));
                } catch (Exception e) {
                    System.err.println("Error parseando fecha: " + datos[18]);
                }
            }

            if (datos.length > 19) h.setNacionalidad(datos[19].trim());

        } catch (Exception e) {
            System.err.println("Error en mapearLineaA_Huesped al mapear línea: " + Arrays.toString(datos) + ". " + e.getMessage());
        }
        return h;
    }

    private List<Huesped> leerTodosLosHuespedesDelArchivo() {
        // Leer todos los huéspedes del archivo CSV
        List<Huesped> huespedes = new ArrayList<>();
        asegurarArchivoExisteConEncabezado();
        // Leer el archivo CSV y mapear cada línea a un objeto Huesped
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_HUESPEDES))) {
            br.readLine();
            String linea;
            while ((linea = br.readLine()) != null) {
                huespedes.add(csvAHuesped(linea));
            }
        } catch (IOException e) {
            System.err.println("DAO: Error al leer todos los huéspedes: " + e.getMessage());
        }
        return huespedes;
    }

    private void escribirArchivoCompleto(List<Huesped> huespedes) {
        // Reescribir todo el archivo CSV con la lista proporcionada de huéspedes
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_HUESPEDES, false))) {
            pw.println("id,nombre,apellido,documento,tipo_doc,email,pais,provincia,localidad,calle,numero,departamento,piso,codigo_postal,cuit,ocupacion,posicion_iva,fecha_nacimiento,nacionalidad");
            for (Huesped h : huespedes) {
                pw.println(huespedACSV(h));
            }
        } catch (IOException e) {
            System.err.println("DAO: Error al reescribir el archivo de huéspedes: " + e.getMessage());
        }
    }

    private String huespedACSV(Huesped h) {
        // Convertir un objeto Huesped a una línea CSV
        StringBuilder sb = new StringBuilder();
        sb.append(h.getId()).append(",")
                .append(h.getNombre()).append(",")
                .append(h.getApellido()).append(",")
                .append(h.getTelefono() != null ? h.getTelefono() : "").append(",")
                .append(h.getDocumento()).append(",")
                .append(h.getTipoDocumento() != null ? h.getTipoDocumento().name() : "").append(",")
                .append(h.getEmail() != null ? h.getEmail() : "").append(",");

        if (h.getDireccion() != null) {
            sb.append(h.getDireccion().getPais() != null ? h.getDireccion().getPais() : "").append(",")
                    .append(h.getDireccion().getProvincia() != null ? h.getDireccion().getProvincia() : "").append(",")
                    .append(h.getDireccion().getLocalidad() != null ? h.getDireccion().getLocalidad() : "").append(",")
                    .append(h.getDireccion().getCalle() != null ? h.getDireccion().getCalle() : "").append(",")
                    .append(h.getDireccion().getNumero()).append(",")
                    .append(h.getDireccion().getDepartamento() != null ? h.getDireccion().getDepartamento() : "").append(",")
                    .append(h.getDireccion().getPiso() != null ? h.getDireccion().getPiso() : "").append(",")
                    .append(h.getDireccion().getCodigoPostal() != null ? h.getDireccion().getCodigoPostal() : "").append(",");
        } else {
            sb.append(",,,,,,,,");
        }

        sb.append(h.getCuit() != null ? h.getCuit() : "").append(",")
                .append(h.getOcupacion() != null ? h.getOcupacion() : "").append(",")
                .append(h.getPosicionIVA() != null ? h.getPosicionIVA() : "").append(",")
                .append(h.getFechaNacimiento() != null ? h.getFechaNacimiento().toString() : "").append(",")
                .append(h.getNacionalidad() != null ? h.getNacionalidad() : "\n");

        return sb.toString();
    }

    private Huesped csvAHuesped(String linea) {
        // Convertir una línea CSV a un objeto Huesped
        String[] datos = linea.split(",");
        return mapearLineaA_Huesped(datos);
    }

    private void asegurarArchivoExisteConEncabezado() {
        // Asegurar que el archivo CSV existe y tiene el encabezado correcto
        File archivo = new File(RUTA_HUESPEDES);
        // Si el archivo no existe, crearlo con el encabezado
        if (!archivo.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                pw.println("id,nombre,apellido,telefono,documento,tipo_doc,email,pais,provincia,localidad,calle,numero,departamento,piso,codigo_postal,cuit,ocupacion,posicion_iva,fecha_nacimiento,nacionalidad");
            } catch (IOException e) {
                System.err.println("DAO: Error al crear archivo de huéspedes: " + e.getMessage());
            }
        }
    }

    private int cargarUltimoId() {
        // Cargar el último ID utilizado en el archivo CSV para inicializar el contador
        List<Huesped> todos = obtenerTodos();
        // Encontrar el ID máximo entre todos los huéspedes
        return todos.stream()
                .map(Huesped::getId)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(0);
    }
}