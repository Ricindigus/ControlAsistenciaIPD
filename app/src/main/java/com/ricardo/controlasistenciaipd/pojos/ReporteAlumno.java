package com.ricardo.controlasistenciaipd.pojos;

import java.util.AbstractMap;
import java.util.ArrayList;

/**
 * Created by RICARDO on 4/03/2017.
 */

public class ReporteAlumno {

    private String nombres;
    private String apellidos;
    private String dni;
    private int edad;
    private ArrayList<Asistencia> asistencias1;
    private ArrayList<Asistencia> asistencias2;



    public ReporteAlumno(){}

    public ReporteAlumno(String nombres, String apellidos, String dni, int edad, ArrayList<Asistencia> asistencias1, ArrayList<Asistencia> asistencias2) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.edad = edad;
        this.asistencias1 = asistencias1;
        this.asistencias2 = asistencias2;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public ArrayList<Asistencia> getAsistencias1() {
        return asistencias1;
    }

    public void setAsistencias1(ArrayList<Asistencia> asistencias1) {
        this.asistencias1 = asistencias1;
    }

    public ArrayList<Asistencia> getAsistencias2() {
        return asistencias2;
    }

    public void setAsistencias2(ArrayList<Asistencia> asistencias2) {
        this.asistencias2 = asistencias2;
    }
}
