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

public class AlumnoAdapter extends RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder> {
    private ArrayList<Alumno> items;

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
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

    public AlumnoAdapter(ArrayList<Alumno> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AlumnoViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_alumno, viewGroup, false);
        return new AlumnoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AlumnoViewHolder viewHolder, int i) {
        viewHolder.numeroAlumno.setText(""+(i+1));
        viewHolder.nombreAlumno.setText(String.valueOf(items.get(i).getNombres() + " " + items.get(i).getApellidos()));
        if(items.get(i).getAsistencia()==false) viewHolder.asistenciaAlumno.setChecked(false);
        else viewHolder.asistenciaAlumno.setChecked(true);
    }



}
