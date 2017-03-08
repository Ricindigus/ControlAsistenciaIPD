package com.ricardo.controlasistenciaipd;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by apoyo03-ui on 2/03/2017.
 */

public class AlumnoReporteAdapter extends RecyclerView.Adapter<AlumnoReporteAdapter.AlumnoViewHolder> {
    private ArrayList<AlumnoReporte> items;

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView numeroAlumno;
        public TextView nombreAlumno;
        public TextView edadAlumno;
        public TextView asistenciaAlumno;

        public AlumnoViewHolder(View v) {
            super(v);
            numeroAlumno = (TextView) v.findViewById(R.id.cardview_reporte_numero);
            nombreAlumno = (TextView) v.findViewById(R.id.cardview_reporte_nombre);
            asistenciaAlumno = (TextView) v.findViewById(R.id.cardview_reporte_asistencia);
        }
    }

    public AlumnoReporteAdapter(ArrayList<AlumnoReporte> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AlumnoReporteAdapter.AlumnoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_alumno_reporte, viewGroup, false);
        return new AlumnoReporteAdapter.AlumnoViewHolder(v);
    }
    @Override
    public void onBindViewHolder(AlumnoReporteAdapter.AlumnoViewHolder viewHolder, int i) {
        viewHolder.numeroAlumno.setText(""+(i+1));
        viewHolder.nombreAlumno.setText(String.valueOf(items.get(i).getNombres() + " " + items.get(i).getApellidos()));
        String asistencias = "";
        String valorAsistencia = "";
        for (int j = 0; j < items.get(i).getAsistencias().length; j++) {
            if(items.get(i).getAsistencias()[j]) valorAsistencia = "A";
            else valorAsistencia = "F";
            if (j > 0) asistencias = asistencias + "-S" + (j+1) + ":" + valorAsistencia;
            else asistencias = asistencias + "S" + (j+1) + ":" + valorAsistencia;
        }
        viewHolder.asistenciaAlumno.setText(asistencias);
    }

}
