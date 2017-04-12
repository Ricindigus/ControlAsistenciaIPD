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
 * Created by apoyo03-ui on 9/03/2017.
 */

public class ListaHorizontalAsistenciasAdapter extends RecyclerView.Adapter<ListaHorizontalAsistenciasAdapter.MyViewHolder> {

    private ArrayList<Asistencia> asistencias;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtView;

        public MyViewHolder(View view) {
            super(view);
            txtView = (TextView) view.findViewById(R.id.txtView);
        }
    }

    public ListaHorizontalAsistenciasAdapter(ArrayList<Asistencia> horizontalList) {
        this.asistencias = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asistencia_horizontal, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtView.setText(asistencias.get(position).getFecha() + ": " + asistencias.get(position).getAsistio());
    }

    @Override
    public int getItemCount() {
        return asistencias.size();
    }
}


