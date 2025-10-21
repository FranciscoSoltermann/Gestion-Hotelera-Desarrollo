package org.TPDesarrollo;

import org.TPDesarrollo.UI.Menu;

/**
 * Clase principal para iniciar la aplicación.
 * Contiene el método main que crea una instancia del menú
 * y llama al método iniciar() para comenzar la interacción
 * con el usuario.
 */
public class Main {
    public static void main(String[] args) {

        Menu menu = new Menu();
        menu.iniciar();
    }
}


