package com.ricardo.controlasistenciaipd.pojos;

import java.util.ArrayList;

/**
 * Created by apoyo03-ui on 31/03/2017.
 */

public class ReporteGeneral {
    private String codigo;
    private String nombres;
    private String apellidos;
    private ArrayList<Asistencia> asistencias;

    public ReporteGeneral(String codigo, String nombres, String apellidos, ArrayList<Asistencia> asistencias) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.asistencias = asistencias;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public ArrayList<Asistencia> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(ArrayList<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }
}
