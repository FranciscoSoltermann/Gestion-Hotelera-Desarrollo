package org.TPDesarrollo.Excepciones;

/**
 * Excepción lanzada cuando se intenta registrar un CUIT que ya existe en el sistema.
 * Indica que el alta del CUIT no fue realizada debido a su existencia previa.
 */
public class CuitExistente extends RuntimeException {
    // Constructor
    public CuitExistente(String cuit) {
        super("El CUIT " + cuit + " ya se encuentra registrado en el sistema. El alta no fue realizada.");
    }

}
