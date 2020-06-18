package com.sr.tasrfb.ui.DBs.Socios;

public class Socio {
    private String Num;
    private String Nombre;
    private int Mz;
    private int Lt;
    private Medidor Medidor;

    public Socio(){

    }

    public String getNum() {
        return Num;
    }

    public void setNum(String num) {
        Num = num;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getMz() {
        return Mz;
    }

    public void setMz(int mz) {
        Mz = mz;
    }

    public int getLt() {
        return Lt;
    }

    public void setLt(int lt) {
        Lt = lt;
    }

    public com.sr.tasrfb.ui.DBs.Socios.Medidor getMedidor() {
        return Medidor;
    }

    public void setMedidor(com.sr.tasrfb.ui.DBs.Socios.Medidor medidor) {
        Medidor = medidor;
    }
}
