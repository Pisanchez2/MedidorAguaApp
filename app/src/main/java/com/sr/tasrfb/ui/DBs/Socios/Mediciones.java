package com.sr.tasrfb.ui.DBs.Socios;

import java.util.Date;

public class Mediciones {

    private ClaseMes Mes;
    private CurrentDate Fecha_Medicion;

    public Mediciones(){

    }

    public CurrentDate getFecha_Medicion() {
        return Fecha_Medicion;
    }

    public void setFecha_Medicion(CurrentDate fechaMedicion) {
        Fecha_Medicion = fechaMedicion;
    }

    public ClaseMes getMes() {
        return Mes;
    }

    public void setMes(ClaseMes mes) {
        Mes = mes;
    }
}
