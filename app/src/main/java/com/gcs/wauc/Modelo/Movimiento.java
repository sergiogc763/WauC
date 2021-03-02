package com.gcs.wauc.Modelo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Entidad Movimiento
 */
public class Movimiento implements Serializable {

    @SerializedName("dniU")
    private String dniU;

    @SerializedName("fecha")
    private Date fecha;

    @SerializedName("hora")
    private String hora;

    @SerializedName("latitud")
    private float latitud;

    @SerializedName("longitud")
    private float longitud;

    public Movimiento(){
    }

    public Movimiento(String dniU, Date fecha, String hora, float latitud, float longitud) {
        this.dniU = dniU;
        this.fecha = fecha;
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getDniU() {
        return dniU;
    }

    public void setDniU(String dniU) {
        this.dniU = dniU;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }
}
