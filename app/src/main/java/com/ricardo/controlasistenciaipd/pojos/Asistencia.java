package com.ricardo.controlasistenciaipd.pojos;

/**
 * Created by apoyo03-ui on 17/03/2017.
 */

public class Asistencia {
    private String fecha;
    private String asistio;

    public Asistencia(String fecha, String asistio) {
        this.fecha = fecha;
        this.asistio = asistio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAsistio() {
        return asistio;
    }

    public void setAsistio(String asistio) {
        this.asistio = asistio;
    }
}
