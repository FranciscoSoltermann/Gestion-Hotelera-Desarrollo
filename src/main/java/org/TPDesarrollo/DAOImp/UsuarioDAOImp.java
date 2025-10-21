package org.TPDesarrollo.DAOImp;

import org.TPDesarrollo.DAOS.UsuarioDAO;
import org.TPDesarrollo.DTOs.UsuarioDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Implementación de la interfaz UsuarioDAO para manejar operaciones relacionadas con usuarios.
 */
public class UsuarioDAOImp implements UsuarioDAO {
    // Ruta del archivo CSV que almacena los datos de los usuarios
    private static final String RUTA_USUARIO = "src/main/resources/usuarios.csv";

    // Método para obtener un usuario por su nombre
    @Override
    public UsuarioDTO obtenerUsuarioPorNombre(String nombre) {
        asegurarArchivoExisteConEncabezado();
        // Leer el archivo CSV y buscar el usuario por nombre
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_USUARIO))) {
            String linea;
            br.readLine();

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 2) {
                    String nombreCSV = datos[0].trim();
                    String contraseniaCSV = datos[1].trim();
                    if (nombreCSV.equalsIgnoreCase(nombre)) {
                        return new UsuarioDTO(nombreCSV, contraseniaCSV);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
        }

        return null;
    }

// Método para asegurar que el archivo CSV existe y tiene el encabezado correcto
    private void asegurarArchivoExisteConEncabezado() {

        File archivo = new File(RUTA_USUARIO);
        // Si el archivo no existe, crearlo con el encabezado
        if (!archivo.exists()) {
            try {
                File directorioPadre = archivo.getParentFile();
                if (directorioPadre != null) {
                    directorioPadre.mkdirs();
                }
                try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
                    pw.println("nombre;contrasenia");
                }
                System.out.println("Archivo 'usuarios.csv' no encontrado. Se ha creado uno nuevo con encabezados.");

            } catch (IOException e) {
                System.err.println("Error crítico al crear el archivo de usuarios: " + e.getMessage());
            }
        }
    }
}