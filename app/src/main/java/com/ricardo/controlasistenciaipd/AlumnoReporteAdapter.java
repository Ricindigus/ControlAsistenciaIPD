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
    private ArrayList<Alumno> items;

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView numeroAlumno;
        public TextView nombreAlumno;
        public TextView asistenciaAlumno;

        public AlumnoViewHolder(View v) {
            super(v);
            numeroAlumno = (TextView) v.findViewById(R.id.cardviewAsistenciaNumero);
            nombreAlumno = (TextView) v.findViewById(R.id.cardViewAsistenciaNombre);
            asistenciaAlumno = (TextView) v.findViewById(R.id.cardViewAsistenciaEstado);
        }
    }

    public AlumnoReporteAdapter(ArrayList<Alumno> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AlumnoReporteAdapter.AlumnoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_confirmar, viewGroup, false);
        return new AlumnoReporteAdapter.AlumnoViewHolder(v);
    }
    @Override
    public void onBindViewHolder(AlumnoReporteAdapter.AlumnoViewHolder viewHolder, int i) {
        viewHolder.numeroAlumno.setText(""+(i+1));
        viewHolder.nombreAlumno.setText(String.valueOf(items.get(i).getNombres() + " " + items.get(i).getApellidos()));
        if(items.get(i).getAsistencia() == true) viewHolder.asistenciaAlumno.setText("A");
        else viewHolder.asistenciaAlumno.setText("F");
    }
}
