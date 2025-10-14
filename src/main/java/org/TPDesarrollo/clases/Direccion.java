package org.TPDesarrollo.clases;

public class Direccion {
    private String pais;
    private String provincia;
    private String localidad;
    private String calle;
    private int numero;
    private String departamento;
    private String piso;
    private String codigoPostal;

    public Direccion(String pais, String provincia, String localidad, String calle, int numero, String departamento, String piso, String codigoPostal) {
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.numero = numero;
        this.departamento = departamento;
        this.piso = piso;
        this.codigoPostal = codigoPostal;
    }

    public int getNumero() {
        return numero;
    }
    public String getPais() {
        return pais;
    }
    public String getProvincia() {
        return provincia;
    }
    public String getLocalidad() {
        return localidad;
    }
    public String getCalle() {
        return calle;
    }
    public String getDepartamento() {
        return departamento;
    }
    public String getPiso() {
        return piso;
    }
    public String getCodigoPostal() {
        return codigoPostal;
    }
    public int setNumero(int numero) {
        return this.numero = numero;
    }
    public String setPais(String pais) {
        return this.pais = pais;
    }
    public String setProvincia(String provincia) {
        return this.provincia = provincia;
    }
    public String setLocalidad(String localidad) {
        return this.localidad = localidad;
    }
    public String setCalle(String calle) {
        return this.calle = calle;
    }
    public String setDepartamento(String departamento) {
        return this.departamento = departamento;
    }
    public String setPiso(String piso) {
        return this.piso = piso;
    }
    public String setCodigoPostal(String codigoPostal) {
        return this.codigoPostal = codigoPostal;
    }



}
