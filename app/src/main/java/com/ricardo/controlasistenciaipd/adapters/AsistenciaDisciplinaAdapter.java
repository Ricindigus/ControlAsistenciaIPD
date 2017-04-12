package com.ricardo.controlasistenciaipd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.pojos.Asistencia;

import java.util.ArrayList;

/**
 * Created by apoyo03-ui on 17/03/2017.
 */

public class AsistenciaDisciplinaAdapter extends RecyclerView.Adapter<AsistenciaDisciplinaAdapter.MyViewHolder> {
    private ArrayList<Asistencia> asistencias;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtFecha;
        TextView txtAsistencia;
        public MyViewHolder(View v) {
            super(v);
            txtFecha = (TextView) v.findViewById(R.id.txt_item_fecha);
            txtAsistencia = (TextView) v.findViewById(R.id.txt_item_asistio);
        }
    }

    public AsistenciaDisciplinaAdapter(ArrayList<Asistencia> items) {
        this.asistencias = items;
    }

    @Override
    public int getItemCount() {
        return asistencias.size();
    }

    @Override
    public AsistenciaDisciplinaAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_asistencia_disciplina, viewGroup, false);
        return new AsistenciaDisciplinaAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AsistenciaDisciplinaAdapter.MyViewHolder viewHolder, int i) {
        viewHolder.txtFecha.setText(asistencias.get(i).getFecha());
        if(asistencias.get(i).getAsistio().equals("F")) viewHolder.txtAsistencia.setText("Faltó");
        else viewHolder.txtAsistencia.setText("Asistió");

    }


}