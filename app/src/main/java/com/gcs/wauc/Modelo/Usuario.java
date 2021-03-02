package com.gcs.wauc.Modelo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Usuario implements Serializable {

    @SerializedName("DNI")
    private String DNI;

    @SerializedName("password")
    private String password;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("telefono")
    private int telefono;

    @SerializedName("puesto")
    private String puesto;

    public Usuario() {
    }

    public Usuario(String DNI, String password, String nombre, int telefono, String puesto) {
        this.DNI = DNI;
        this.password = password;
        this.nombre = nombre;
        this.telefono = telefono;
        this.puesto = puesto;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}
