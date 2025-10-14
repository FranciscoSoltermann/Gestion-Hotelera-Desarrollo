package org.TPDesarrollo.daoImps;

import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.daos.HuespedDAO;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class HuespedDAOImp implements HuespedDAO {

    private static final String RUTA_HUESPEDES = "src/main/resources/huespedes.csv";
    // Usaremos un archivo separado para llevar el control del último ID y evitar leer todo el archivo solo para eso.
    private static final String RUTA_CONTADOR = "src/main/resources/huesped_id_counter.txt";
    private final AtomicInteger contadorId;

    public HuespedDAOImp() {
        // Al iniciar el DAO, cargamos el último ID guardado.
        this.contadorId = new AtomicInteger(cargarUltimoId());
    }

    @Override
    public void darDeAltaHuesped(Huesped huesped) {
        asegurarArchivoExisteConEncabezado();
        huesped.setId(contadorId.incrementAndGet()); // Asignamos un ID nuevo

        // Usamos try-with-resources en modo 'append' para añadir al final.
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_HUESPEDES, true))) {
            pw.println(huespedACSV(huesped));
            guardarUltimoId(huesped.getId()); // Guardamos el nuevo ID máximo
            System.out.println("DAO: Huésped " + huesped.getApellido() + " guardado en CSV con ID: " + huesped.getId());
        } catch (IOException e) {
            System.err.println("DAO: Error al dar de alta huésped: " + e.getMessage());
        }
    }

    @Override
    public Huesped buscarHuesped(String apellido, String nombre, String tipoDocumento, Integer documento) {
        asegurarArchivoExisteConEncabezado();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_HUESPEDES))) {
            String linea;
            br.readLine(); // Omitir encabezado
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                // Comparamos los campos relevantes. ¡Asegúrate de que el orden sea el correcto!
                String apellidoCSV = datos[2];
                String nombreCSV = datos[1];

                if (apellidoCSV.equalsIgnoreCase(apellido) && nombreCSV.equalsIgnoreCase(nombre)) {
                    return csvAHuesped(linea); // Convertimos la línea encontrada a un objeto Huesped
                }
            }
        } catch (IOException e) {
            System.err.println("DAO: Error al buscar huésped: " + e.getMessage());
        }
        return null; // No se encontró
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

        // Usamos streams y lambda para filtrar la lista (requisito del TP) [cite: 11]
        List<Huesped> huespedesRestantes = todosLosHuespedes.stream()
                .filter(h -> !Objects.equals(h.getId(), id))
                .collect(Collectors.toList());

        // Si el tamaño de la lista cambió, significa que se eliminó un huésped.
        if (huespedesRestantes.size() < todosLosHuespedes.size()) {
            escribirArchivoCompleto(huespedesRestantes);
            System.out.println("DAO: Huésped con ID " + id + " dado de baja del CSV.");
        }
    }

    // --- MÉTODOS PRIVADOS AUXILIARES ---

    private List<Huesped> leerTodosLosHuespedesDelArchivo() {
        List<Huesped> huespedes = new ArrayList<>();
        asegurarArchivoExisteConEncabezado();
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_HUESPEDES))) {
            br.readLine(); // Omitir encabezado
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
        // Escribe la lista entera, sobreescribiendo el archivo.
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_HUESPEDES, false))) {
            pw.println("id,nombre,apellido,documento,tipo_doc,nacionalidad"); // Escribe el encabezado
            for (Huesped h : huespedes) {
                pw.println(huespedACSV(h));
            }
        } catch (IOException e) {
            System.err.println("DAO: Error al reescribir el archivo de huéspedes: " + e.getMessage());
        }
    }

    private String huespedACSV(Huesped h) {
        // Convierte un objeto Huesped a una línea de texto CSV.
        // ¡IMPORTANTE! El orden debe ser consistente.
        return h.getId() + "," +
                h.getNombre() + "," +
                h.getApellido() + "," +
                h.getDocumento() + "," +
                (h.getTipoDocumento() != null ? h.getTipoDocumento().name() : "") + "," +
                h.getNacionalidad();
    }

    private Huesped csvAHuesped(String linea) {
        String[] datos = linea.split(",");
        Huesped h = new Huesped();
        h.setId(Integer.parseInt(datos[0]));
        h.setNombre(datos[1]);
        h.setApellido(datos[2]);
        h.setDocumento(Integer.parseInt(datos[3]));
        // Aquí deberías convertir el String a tu Enum TipoDocumento
        // h.setTipoDocumento(TipoDocumento.valueOf(datos[4]));
        h.setNacionalidad(datos[5]);
        return h;
    }

    private void asegurarArchivoExisteConEncabezado() {
        File archivo = new File(RUTA_HUESPEDES);
        if (!archivo.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                pw.println("id,nombre,apellido,documento,tipo_doc,nacionalidad");
            } catch (IOException e) {
                System.err.println("DAO: Error al crear archivo de huéspedes: " + e.getMessage());
            }
        }
    }

    private int cargarUltimoId() {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_CONTADOR))) {
            return Integer.parseInt(br.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0; // Si el archivo no existe o está vacío, empezamos en 0.
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