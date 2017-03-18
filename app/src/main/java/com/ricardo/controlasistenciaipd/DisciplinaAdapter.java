package com.ricardo.controlasistenciaipd;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by apoyo03-ui on 17/03/2017.
 */

public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.MyViewHolder> {
    private ArrayList<Asistencia> items;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        TextView txtFecha;
        TextView txtAsistencia;

        public MyViewHolder(View v) {
            super(v);
            txtFecha = (TextView) v.findViewById(R.id.txt_item_fecha);
            txtAsistencia = (TextView) v.findViewById(R.id.txt_item_asistio);
        }
    }

    public DisciplinaAdapter(ArrayList<Asistencia> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public DisciplinaAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_reporte_fecha, viewGroup, false);
        return new DisciplinaAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DisciplinaAdapter.MyViewHolder viewHolder, int i) {
        viewHolder.txtFecha.setText(items.get(i).getFecha());
        if(!items.get(i).getAsistencia()) viewHolder.txtAsistencia.setText("Faltó");
        else viewHolder.txtAsistencia.setText("Asistió");

    }


}