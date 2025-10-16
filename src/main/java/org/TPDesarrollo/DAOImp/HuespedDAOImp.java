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

public class HuespedDAOImp implements HuespedDAO {

    private static final String RUTA_HUESPEDES = "src/main/resources/huespedes.csv";
    private static final String RUTA_CONTADOR = "src/main/resources/huesped_id_counter.txt";
    private final AtomicInteger contadorId;

    public HuespedDAOImp() {
        this.contadorId = new AtomicInteger(cargarUltimoId());
    }

    @Override
    public void darDeAltaHuesped(Huesped huesped) {
        asegurarArchivoExisteConEncabezado();
        huesped.setId(contadorId.incrementAndGet());

        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_HUESPEDES, true))) {
            pw.println(huespedACSV(huesped));
            guardarUltimoId(huesped.getId());
            System.out.println("DAO: Huésped " + huesped.getApellido() + " guardado en CSV con ID: " + huesped.getId());
        } catch (IOException e) {
            System.err.println("DAO: Error al dar de alta huésped: " + e.getMessage());
        }
    }

    @Override
    public List<Huesped> buscarHuespedes(String apellido, String nombre, String tipoDocumento, Integer documento) {
        List<Huesped> todosLosHuespedes = obtenerTodos();

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
        return obtenerTodos().stream()
                .filter(h -> Objects.equals(h.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void modificarHuesped(Huesped huespedModificado) {
        List<Huesped> todosLosHuespedes = leerTodosLosHuespedesDelArchivo();
        boolean modificado = false;

        for (int i = 0; i < todosLosHuespedes.size(); i++) {
            if (Objects.equals(todosLosHuespedes.get(i).getId(), huespedModificado.getId())) {
                todosLosHuespedes.set(i, huespedModificado);
                modificado = true;
                break;
            }
        }

        if (modificado) {
            escribirArchivoCompleto(todosLosHuespedes);
            System.out.println("DAO: Huésped con ID " + huespedModificado.getId() + " modificado en CSV.");
        }
    }

    @Override
    public void darDeBajaHuesped(Integer id) {
        List<Huesped> todosLosHuespedes = leerTodosLosHuespedesDelArchivo();

        List<Huesped> huespedesRestantes = todosLosHuespedes.stream()
                .filter(h -> !Objects.equals(h.getId(), id))
                .collect(Collectors.toList());

        if (huespedesRestantes.size() < todosLosHuespedes.size()) {
            escribirArchivoCompleto(huespedesRestantes);
            System.out.println("DAO: Huésped con ID " + id + " dado de baja del CSV.");
        }
    }

    // --- MÉTODOS PRIVADOS AUXILIARES ---

    private List<Huesped> obtenerTodos() {
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
        Huesped h = new Huesped();
        try {
            h.setId(Integer.parseInt(datos[0].trim()));
            h.setNombre(datos[1].trim());
            h.setApellido(datos[2].trim());

            try {
                h.setDocumento(Integer.parseInt(datos[3].replaceAll("[^0-9]", "")));
            } catch (NumberFormatException e) {
                h.setDocumento(0);
            }

            try {
                h.setTipoDocumento(TipoDocumento.valueOf(datos[4].trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                h.setTipoDocumento(TipoDocumento.DNI);
            }

            if (datos.length > 5) h.setEmail(datos[5].trim());

            if (datos.length > 13) {
                Direccion direccion = new Direccion(
                        datos.length > 6 ? datos[6].trim() : "",  // pais
                        datos.length > 7 ? datos[7].trim() : "",  // provincia
                        datos.length > 8 ? datos[8].trim() : "",  // localidad
                        datos.length > 9 ? datos[9].trim() : "",  // calle
                        datos.length > 10 && !datos[10].trim().isEmpty() ? Integer.parseInt(datos[10].trim()) : 0,  // numero
                        datos.length > 11 ? datos[11].trim() : "",  // departamento
                        datos.length > 12 ? datos[12].trim() : "",  // piso
                        datos.length > 13 ? datos[13].trim() : ""   // codigoPostal
                );
                h.setDireccion(direccion);
            }

            if (datos.length > 14) h.setCuit(datos[14].trim());
            if (datos.length > 15) h.setOcupacion(datos[15].trim());
            if (datos.length > 16) h.setPosicionIVA(datos[16].trim());

            if (datos.length > 17 && !datos[17].trim().isEmpty()) {
                try {
                    h.setFechaNacimiento(LocalDate.parse(datos[17].trim()));
                } catch (Exception e) {
                    System.err.println("Error parseando fecha: " + datos[17]);
                }
            }

            if (datos.length > 18) h.setNacionalidad(datos[18].trim());

        } catch (Exception e) {
            System.err.println("Error en mapearLineaA_Huesped al mapear línea: " + Arrays.toString(datos) + ". " + e.getMessage());
        }
        return h;
    }

    private List<Huesped> leerTodosLosHuespedesDelArchivo() {
        List<Huesped> huespedes = new ArrayList<>();
        asegurarArchivoExisteConEncabezado();
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
        StringBuilder sb = new StringBuilder();
        sb.append(h.getId()).append(",")
                .append(h.getNombre()).append(",")
                .append(h.getApellido()).append(",")
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
                .append(h.getNacionalidad() != null ? h.getNacionalidad() : "");

        return sb.toString();
    }

    private Huesped csvAHuesped(String linea) {
        String[] datos = linea.split(",");
        return mapearLineaA_Huesped(datos);
    }

    private void asegurarArchivoExisteConEncabezado() {
        File archivo = new File(RUTA_HUESPEDES);
        if (!archivo.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                pw.println("id,nombre,apellido,documento,tipo_doc,email,pais,provincia,localidad,calle,numero,departamento,piso,codigo_postal,cuit,ocupacion,posicion_iva,fecha_nacimiento,nacionalidad");
            } catch (IOException e) {
                System.err.println("DAO: Error al crear archivo de huéspedes: " + e.getMessage());
            }
        }
    }

    private int cargarUltimoId() {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_CONTADOR))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    private void guardarUltimoId(int id) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_CONTADOR))) {
            pw.print(id);
        } catch (IOException e) {
            System.err.println("DAO: Error al guardar el contador de ID: " + e.getMessage());
        }
    }
}