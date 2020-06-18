package com.sr.tasrfb.ui.DBs.Socios;

import java.util.Date;

public class Medidor {

    private String Num;
    private String Fecha_Instalacion;


    public Medidor(){

    }

    public String getNum() {
        return Num;
    }

    public void setNum(String num) {
        Num = num;
    }

    public String getFecha_Instalacion() {
        return Fecha_Instalacion;
    }

    public void setFecha_Instalacion(String fecha_Instalacion) {
        Fecha_Instalacion = fecha_Instalacion;
    }
}
