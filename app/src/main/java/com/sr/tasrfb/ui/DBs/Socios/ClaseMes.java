package com.sr.tasrfb.ui.DBs.Socios;

public class ClaseMes {

private float Medicion;
private String URL;
private String Fecha_Medicion;

    public ClaseMes(){

    }

    public String getFecha_Medicion() {
        return Fecha_Medicion;
    }

    public void setFecha_Medicion(String fecha_Medicion) {
        Fecha_Medicion = fecha_Medicion;
    }

    public float getMedicion() {
        return Medicion;
    }

    public void setMedicion(float medicion) {
        Medicion = medicion;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
