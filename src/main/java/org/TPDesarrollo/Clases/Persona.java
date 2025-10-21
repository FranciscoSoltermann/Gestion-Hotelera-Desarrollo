package org.TPDesarrollo.Clases;

import org.TPDesarrollo.Enums.TipoDocumento;

/**
 * Clase abstracta que representa una persona con atributos comunes.
 */
public abstract class Persona {
    // Atributos comunes de una persona
    protected String nombre;
    protected String apellido;
    protected String telefono;
    protected Integer documento;
    protected TipoDocumento tipoDocumento;
// Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
