package org.TPDesarrollo.Clases;

import org.TPDesarrollo.Enums.TipoDocumento;

import java.time.LocalDate;

/**
 * Clase que representa a un huésped, que es una persona con información adicional.
 * Hereda de la clase Persona.
 */
public class Huesped extends Persona {
    // Campos adicionales específicos de Huesped
    private String email;
    private Direccion direccion;
    private String cuit;
    private String ocupacion;
    private String posicionIVA;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private Integer id;
// Constructor vacío
    public Huesped() {}

    // Getters y Setters para todos los campos...
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public Integer getDocumento() { return documento; }
    public void setDocumento(Integer documento) { this.documento = documento; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public String getPosicionIVA() { return posicionIVA; }
    public void setPosicionIVA(String posicionIVA) { this.posicionIVA = posicionIVA; }

}
