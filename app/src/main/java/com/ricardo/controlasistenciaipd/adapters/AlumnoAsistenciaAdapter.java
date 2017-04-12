package com.ricardo.controlasistenciaipd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.pojos.Alumno;

import java.util.ArrayList;

/**
 * Created by RICARDO on 20/12/2016.
 */

public class AlumnoAsistenciaAdapter extends RecyclerView.Adapter<AlumnoAsistenciaAdapter.AlumnoViewHolder> {
    private ArrayList<Alumno> alumnosAsistencia;

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        public TextView numeroAlumno;
        public TextView nombreAlumno;
        public CheckBox asistenciaAlumno;

        public AlumnoViewHolder(View v) {
            super(v);
            numeroAlumno = (TextView) v.findViewById(R.id.cardviewNumero);
            nombreAlumno = (TextView) v.findViewById(R.id.cardViewNombre);
            asistenciaAlumno = (CheckBox) v.findViewById(R.id.cardViewAsistencia);
        }
    }

    public AlumnoAsistenciaAdapter(ArrayList<Alumno> items) {
        this.alumnosAsistencia = items;
    }

    @Override
    public int getItemCount() {
        return alumnosAsistencia.size();
    }

    @Override
    public AlumnoViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_alumno_asistencia, viewGroup, false);
        return new AlumnoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlumnoViewHolder viewHolder, int i) {
        viewHolder.numeroAlumno.setText(""+(i+1));
        viewHolder.nombreAlumno.setText(String.valueOf(alumnosAsistencia.get(i).getNombres() + " "
                + alumnosAsistencia.get(i).getApellidos()).toUpperCase());
        if(alumnosAsistencia.get(i).getAsistencia()==false) viewHolder.asistenciaAlumno.setChecked(false);
        else viewHolder.asistenciaAlumno.setChecked(true);
    }
}
