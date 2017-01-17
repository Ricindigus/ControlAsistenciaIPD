package com.ricardo.controlasistenciaipd;

import java.io.Serializable;

/**
 * Created by RICARDO on 20/12/2016.
 */

public class Alumno implements Serializable {
    private String codigo;
    private String nombres;
    private String apellidos;
    private boolean asistencia;

    public Alumno(String nombres, String apellidos) {
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public Alumno(String codigo, String nombres, String apellidos, boolean asistencia) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.asistencia = asistencia;
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

    public boolean getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
    }

}