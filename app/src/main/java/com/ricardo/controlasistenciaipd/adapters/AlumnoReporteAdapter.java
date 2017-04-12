package com.ricardo.controlasistenciaipd.adapters;

import android.app.Application;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.pojos.Asistencia;
import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.pojos.ReporteGeneral;

import java.util.ArrayList;

/**
 * Created by apoyo03-ui on 2/03/2017.
 */

public class AlumnoReporteAdapter extends RecyclerView.Adapter<AlumnoReporteAdapter.AlumnoViewHolder> {
    private ArrayList<ReporteGeneral> alumnosReporte;
    private Application application;

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        public TextView numeroAlumno;
        public TextView nombreAlumno;
        public RecyclerView recyclerViewAsistencias;

        public AlumnoViewHolder(View v) {
            super(v);
            numeroAlumno = (TextView) v.findViewById(R.id.cardview_reporte_numero);
            nombreAlumno = (TextView) v.findViewById(R.id.cardview_reporte_nombre);
            recyclerViewAsistencias = (RecyclerView) v.findViewById(R.id.cardview_reporte_recycler);
        }
    }

    public AlumnoReporteAdapter(ArrayList<ReporteGeneral> items, Application application) {
        this.alumnosReporte = items;
        this.application = application;
    }


    @Override
    public AlumnoReporteAdapter.AlumnoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_alumno_reporte_general, viewGroup, false);
        return new AlumnoReporteAdapter.AlumnoViewHolder(v);
    }
    @Override
    public void onBindViewHolder(AlumnoReporteAdapter.AlumnoViewHolder viewHolder, int i) {
        viewHolder.numeroAlumno.setText(""+(i+1));
        viewHolder.nombreAlumno.setText(String.valueOf(alumnosReporte.get(i).getNombres().toUpperCase() + " " + alumnosReporte.get(i).getApellidos().toUpperCase()));
        ArrayList<Asistencia> horizontalList = new ArrayList<Asistencia>();
        horizontalList = alumnosReporte.get(i).getAsistencias();
        if(horizontalList.size() == 0) horizontalList.add(new Asistencia("Registros encontrados" + "","0"));
        ListaHorizontalAsistenciasAdapter horizontalAdapter = new ListaHorizontalAsistenciasAdapter(horizontalList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false);
        viewHolder.recyclerViewAsistencias.setLayoutManager(horizontalLayoutManager);
        viewHolder.recyclerViewAsistencias.setAdapter(horizontalAdapter);
    }

    @Override
    public int getItemCount() {
        return alumnosReporte.size();
    }

}
