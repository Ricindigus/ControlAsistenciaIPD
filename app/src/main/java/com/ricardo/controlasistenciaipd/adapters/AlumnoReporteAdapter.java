package com.ricardo.controlasistenciaipd.adapters;

import android.app.Application;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ricardo.controlasistenciaipd.pojos.Asistencia;
import com.ricardo.controlasistenciaipd.pojos.ReporteAlumno;
import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.pojos.ReporteGeneral;

import java.util.ArrayList;

/**
 * Created by apoyo03-ui on 2/03/2017.
 */

public class AlumnoReporteAdapter extends RecyclerView.Adapter<AlumnoReporteAdapter.AlumnoViewHolder> {
    private ArrayList<ReporteGeneral> items;
    private Application application;

    public static class AlumnoViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
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
        this.items = items;
        this.application = application;
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
        viewHolder.nombreAlumno.setText(String.valueOf(items.get(i).getNombres().toUpperCase() + " " + items.get(i).getApellidos().toUpperCase()));
        ArrayList<Asistencia> horizontalList = new ArrayList<Asistencia>();
        horizontalList = items.get(i).getAsistencias();
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
//        horizontalList.add(new Asistencia("13/02/2017","A"));
        if(horizontalList.size() == 0) horizontalList.add(new Asistencia("Registros encontrados" +
                "","0"));
        AsistenciasAdapter horizontalAdapter = new AsistenciasAdapter(horizontalList);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(application, LinearLayoutManager.HORIZONTAL, false);
        viewHolder.recyclerViewAsistencias.setLayoutManager(horizontalLayoutManager);
        viewHolder.recyclerViewAsistencias.setAdapter(horizontalAdapter);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
