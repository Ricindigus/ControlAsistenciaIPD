package com.ricardo.controlasistenciaipd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.pojos.Alumno;

import java.util.ArrayList;

/**
 * Created by apoyo03-ui on 15/03/2017.
 */

public class BuscadorAlumnosAdapter extends RecyclerView.Adapter<BuscadorAlumnosAdapter.ViewHolder>{
    private ArrayList<Alumno> alumnos;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private View cardView;
        public ViewHolder(View v) {
            super(v);
            cardView = v;
            textView = (TextView) v.findViewById(R.id.txt_item_alumno);
        }
    }

    public BuscadorAlumnosAdapter(ArrayList<Alumno> alumnos, OnItemClickListener onItemClickListener) {
        this.alumnos = alumnos;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public BuscadorAlumnosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buscador_alumno, parent, false);
        return new BuscadorAlumnosAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textView.setText(alumnos.get(position).getNombres().toUpperCase()
                + " " + alumnos.get(position).getApellidos().toUpperCase());
        final int pos = position;
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

}
