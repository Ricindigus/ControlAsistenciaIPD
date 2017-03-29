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

public class BuscadorAdapter extends RecyclerView.Adapter<BuscadorAdapter.ViewHolder>{
    private ArrayList<Alumno> alumnos;
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView textView;
        private View cardView;
        public ViewHolder(View v) {
            super(v);
            cardView = v;
            textView = (TextView) v.findViewById(R.id.txt_item_alumno);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BuscadorAdapter(ArrayList<Alumno> alumnos,OnItemClickListener onItemClickListener) {
        this.alumnos = alumnos;
        mOnItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BuscadorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte_alumno, parent, false);
        return new BuscadorAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.textView.setText(alumnos.get(position).getNombres() + " " + alumnos.get(position).getApellidos());
        final int pos = position;
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, pos);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return alumnos.size();
    }

}
