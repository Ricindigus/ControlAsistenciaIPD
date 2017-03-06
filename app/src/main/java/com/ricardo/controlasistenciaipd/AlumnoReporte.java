package com.ricardo.controlasistenciaipd;

/**
 * Created by RICARDO on 4/03/2017.
 */

public class AlumnoReporte {
    private String nombres;
    private String apellidos;
    private int edad;
    private boolean[] asistencias;

    public AlumnoReporte(){}

    public AlumnoReporte(String nombres, String apellidos, int edad, boolean[] asistencias) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.asistencias = asistencias;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public boolean[] getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(boolean[] asistencias) {
        this.asistencias = asistencias;
    }
}
