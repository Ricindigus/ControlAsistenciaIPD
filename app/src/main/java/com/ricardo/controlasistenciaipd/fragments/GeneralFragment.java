package com.ricardo.controlasistenciaipd.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.Alumno;
import com.ricardo.controlasistenciaipd.AlumnoAdapter;
import com.ricardo.controlasistenciaipd.AlumnoReporte;
import com.ricardo.controlasistenciaipd.AlumnoReporteAdapter;
import com.ricardo.controlasistenciaipd.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralFragment extends Fragment {
    int dia, mes, anio;
    ImageButton btnCalendar1;
    ImageButton btnCalendar2;
    TextView txtFecha1;
    TextView txtFecha2;
    Spinner spHorarios;
    Spinner spComplejos;
    private ArrayList<String> complejos;
    private ArrayList<String> horarios;
    private ArrayList<AlumnoReporte> alumnos;
    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager lManager;
    View view;
    public GeneralFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_general, container, false);
        horarios = new ArrayList<String>();
        complejos = new ArrayList<String>();
        alumnos = new ArrayList<AlumnoReporte>();

        horarios.add("LUN 14:00-15:00,MIE 14:00-15:00,VIE 14:00-15:00,SAB 14:00-15:00");
        horarios.add("LUN 15:00-16:00,MIE 15:00-16:00,VIE 15:00-16:00");
        horarios.add("LUN 16:00-17:00,MIE 16:00-17:00,VIE 16:00-17:00");
        horarios.add("MAR/JUE 14:00-15:00");
        horarios.add("MAR/JUE 15:00-16:00");
        horarios.add("MAR/JUE 16:00-17:00");
        horarios.add("SAB 08:00-09:00");
        horarios.add("SAB 09:00-10:00");
        horarios.add("SAB 10:00-11:00");

        complejos.add("ESTADIO NACIONAL");
        complejos.add("ESTADIO DELLE ALPHI");
        complejos.add("ESTADIO CAMP NOU");
        complejos.add("EMIRATES STADIUM");
        complejos.add("ESTADIO SANTIAGO BERNABEU");

        boolean[] arrayAsistencias = {true,true,true,true,true,true,true,true,true};
        alumnos.add(new AlumnoReporte("Denis Ricardo","Morales Retamozo",6,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Alan Arnold","Ramos Gonzales",7,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Denis Ricardo","Morales Retamozo",6,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Alan Arnold","Ramos Gonzales",7,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Denis Ricardo","Morales Retamozo",6,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Alan Arnold","Ramos Gonzales",7,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Denis Ricardo","Morales Retamozo",6,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Alan Arnold","Ramos Gonzales",7,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Denis Ricardo","Morales Retamozo",6,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Alan Arnold","Ramos Gonzales",7,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Denis Ricardo","Morales Retamozo",6,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Alan Arnold","Ramos Gonzales",7,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Denis Ricardo","Morales Retamozo",6,arrayAsistencias));
        alumnos.add(new AlumnoReporte("Alan Arnold","Ramos Gonzales",7,arrayAsistencias));

        spHorarios = (Spinner)view.findViewById(R.id.sp_reporte_horario);
        spComplejos = (Spinner)view.findViewById(R.id.sp_reporte_complejo);
        cargarSpiner(complejos, 0);
        cargarSpiner(horarios, 1);

//        //recuperando codigo
//        final Bundle recupera=getIntent().getExtras();
//        if(recupera!=null){
//            recuperado=recupera.getString("cod");
//        }
        btnCalendar1 = (ImageButton)view.findViewById(R.id.btn_calendario1);
        btnCalendar2 = (ImageButton)view.findViewById(R.id.btn_calendario2);
        txtFecha1 = (TextView)view.findViewById(R.id.txtFecha1);
        txtFecha2 = (TextView)view.findViewById(R.id.txtFecha2);


        Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);
        String mAux = "" + mes;
        if(mes < 10) mAux = "0" + mAux;
        String dAux = "" + dia;
        if(dia < 10) dAux = "0" + dAux;

        txtFecha1.setText(dAux + "/" + mAux + "/" + anio);
        txtFecha2.setText(dAux + "/" + mAux + "/" + anio);

        btnCalendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++ ;
                        String d = "" + dayOfMonth;
                        String m = "" + month;
                        if(month < 10) m = "0" + m;
                        if(dayOfMonth < 10) d = "0" + d;
                        txtFecha1.setText(d + "/" + m + "/" + year);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });
        btnCalendar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++ ;
                        String d = "" + dayOfMonth;
                        String m = "" + month;
                        if(month < 10) m = "0" + m;
                        if(dayOfMonth < 10) d = "0" + d;
                        txtFecha2.setText(d + "/" + m + "/" + year);
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });
        cargarRecyclerView(alumnos);
        return view;
    }
    public void cargarSpiner(ArrayList<String> datos, int spinner){
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(getContext(),R.layout.custom_spinner,datos);
        if(spinner == 0)
            spComplejos.setAdapter(adaptador);
        else
            spHorarios.setAdapter(adaptador);
    }

    public void cargarRecyclerView(ArrayList<AlumnoReporte> datos){

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());

        // Crear un nuevo adaptador
        adapter = new AlumnoReporteAdapter(datos);

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.recyclerview_reporte);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }
}
