package org.TPDesarrollo.UI;

/**
 * Interfaz que define una acción ejecutable desde un menú de consola.
 * Cada implementación de esta interfaz debe proporcionar la lógica
 * para el método ejecutar(), que será invocado cuando se seleccione
 * la opción correspondiente en el menú.
 */
public interface AccionMenu {
    void ejecutar();
}