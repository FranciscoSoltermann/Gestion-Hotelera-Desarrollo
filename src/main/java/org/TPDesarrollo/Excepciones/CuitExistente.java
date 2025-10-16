package org.TPDesarrollo.Excepciones;

public class CuitExistente extends RuntimeException {

    public CuitExistente(String cuit) {
        super("El CUIT " + cuit + " ya se encuentra registrado en el sistema. El alta no fue realizada.");
    }

}
