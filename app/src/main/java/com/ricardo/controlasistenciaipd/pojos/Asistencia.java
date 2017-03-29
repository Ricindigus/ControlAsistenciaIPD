package com.ricardo.controlasistenciaipd.pojos;

/**
 * Created by apoyo03-ui on 17/03/2017.
 */

public class Asistencia {
    private String fecha;
    private boolean asistio;

    public Asistencia(String fecha, boolean asistio) {
        this.fecha = fecha;
        this.asistio = asistio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean getAsistencia() {
        return asistio;
    }

    public void setAsistio(boolean asistio) {
        this.asistio = asistio;
    }
}
