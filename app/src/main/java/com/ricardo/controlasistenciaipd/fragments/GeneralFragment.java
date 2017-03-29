package com.ricardo.controlasistenciaipd.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ricardo.controlasistenciaipd.pojos.AlumnoReporte;
import com.ricardo.controlasistenciaipd.adapters.AlumnoReporteAdapter;
import com.ricardo.controlasistenciaipd.R;

import org.json.JSONArray;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralFragment extends Fragment {
    String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";
    //String hostIpdProduccion = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    //String hostlocal = "http://10.10.118.16//WebServiceAndroid/";
    //String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";

    int dia, mes, anio;
    ImageButton btnCalendar1;
    ImageButton btnCalendar2;
    TextView txtFecha1, txtFecha2;
    TextView txtEvento, txtDocDisciplina;
    Spinner spHorarios;
    Spinner spComplejos;
    private ArrayList<String> complejos;
    private ArrayList<String> horarios;
    private ArrayList<AlumnoReporte> alumnos;
    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager lManager;
    View view;
    String recuperado = "", evento =  "", nomEvento = "";


    public GeneralFragment(){}

    @SuppressLint("ValidFragment")
    public GeneralFragment(Bundle recupera) {
        // Required empty public constructor
        final Bundle r = recupera;
        if(r!=null){
            recuperado = recupera.getString("codigoPonente");
            evento = recupera.getString("codigoEvento");
            nomEvento = recupera.getString("nombreEvento");
        }
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

//        btnCalendar1 = (ImageButton)view.findViewById(R.id.btn_calendario1);
//        btnCalendar2 = (ImageButton)view.findViewById(R.id.btn_calendario2);
        txtFecha1 = (TextView)view.findViewById(R.id.txtFecha1);
        txtFecha2 = (TextView)view.findViewById(R.id.txtFecha2);
        txtEvento = (TextView)view.findViewById(R.id.txt_reportes_evento);
        txtDocDisciplina = (TextView)view.findViewById(R.id.txt_reportes_docente_disciplina);

        cargarSpiner(complejos, 0);
        cargarSpiner(horarios, 1);

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

        Thread thread = new Thread(){
            @Override
            public void run() {
                final String resDetalles = traerDetalles(evento, recuperado);
//                final String resComplejos = traerComplejos(recuperadoCodEvento,recuperadoCodDocente);
//                try {
//                    JSONArray jsonArray = new JSONArray(resComplejos);
//                    codComplejo = jsonArray.getJSONObject(0).getString("id_complejo");
//                    descripcionComplejo = jsonArray.getJSONObject(0).getString("complejo");
//                } catch (JSONException e) {}
//                final String resHorarios = traerHorarios(recuperadoCodEvento,codComplejo,recuperadoCodDocente);
//                JSONArray jsonArray = null;
//                try {
//                    jsonArray = new JSONArray(resHorarios);
//                    codHorario = jsonArray.getJSONObject(0).getString("id_disciplinaevento");
//                    descripcionHorario = jsonArray.getJSONObject(0).getString("horario");
//                } catch (JSONException e) {}
//                final String resAlumnos = traerAlumnos(recuperadoCodEvento,codHorario);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mostrarDetalles(resDetalles);
//                        cargarSpiner(ArregloSpiner(resComplejos,0),0);
//                        cargarSpiner(ArregloSpiner(resHorarios,1),1);
//                        cargarRecyclerView(ArregloLista(resAlumnos));
                    }
                });
            }
        };
        thread.start();
        txtFecha1.setOnClickListener(new View.OnClickListener() {
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
        txtFecha2.setOnClickListener(new View.OnClickListener() {
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

    public String traerDetalles(String codEve, String codDoc){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "ListarDetalle.php?ide=" + codEve + "&cod=" + codDoc);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }
        }catch(Exception e){}
        return resul.toString();
    }

    public String traerComplejos(String codigoEve, String codDoc){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "ListarComplejos.php?ide="+codigoEve+"&cod="+codDoc);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }
        }catch(Exception e){}
        return resul.toString();
    }

    public String traerHorarios(String codEve, String codCom, String codDoc){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "ListarHorarios.php?ide=" + codEve + "&cpj=" + codCom + "&cod=" + codDoc);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }
        }catch(Exception e){}
        return resul.toString();
    }


    //METODO QUE PERMITE MOSTRAR EL NOMBRE
    public void mostrarDetalles(String response){
        try{
            String dDocente = "";
            String dDisciplina = "";
            JSONArray json = new JSONArray(response);
            for(int i = 0; i < json.length(); i++){
                dDocente = json.getJSONObject(i).getString("nombres") + " " +json.getJSONObject(i).getString("apepaterno");
                dDisciplina = json.getJSONObject(i).getString("curso");
                txtEvento.setText(nomEvento);
                txtDocDisciplina.setText(dDocente + "-" + dDisciplina);
            }
        }catch(Exception e){}
    }

//    public ArrayList<String> ArregloSpiner(String response, int spinner){
//        ArrayList<String> listado=new ArrayList<String>();
//        ArrayList<String> ids = new ArrayList<String>();
//        if(spinner == 1){
//            try{
//                JSONArray json=new JSONArray(response);
//                String texto="";
//                for(int i=0; i<json.length();i++){
//                    codHorario = json.getJSONObject(i).getString("id_disciplinaevento");
//                    ids.add(codHorario);
//                    texto=json.getJSONObject(i).getString("horario");
//                    listado.add(texto);
//                }
//                idHorarios = ids;
//                descHorarios = listado;
//            }catch(Exception e){}
//        }else{
//            try{
//                JSONArray json=new JSONArray(response);
//                String texto="";
//                for(int i=0; i<json.length();i++){
//                    codComplejo = json.getJSONObject(i).getString("id_complejo");
//                    ids.add(codComplejo);
//                    texto=json.getJSONObject(i).getString("complejo");
//                    listado.add(texto);
//                }
//                idComplejos = ids;
//                descComplejos = listado;
//            }catch(Exception e){}
//        }
//        return listado;
//    }
//    //METODO QUE PERMITE CARGAR EL SPINNER
//
//    public void cargarSpiner(ArrayList<String> datos, int spinner){
//        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.custom_spinner,datos);
//        if(spinner == 1) spHorarios.setAdapter(adaptador);
//        else spComplejos.setAdapter(adaptador);
//    }
//    public ArrayList<Alumno> ArregloLista(String response){
//        try{
//            JSONArray json=new JSONArray(response);
//            String alumno = null;
//            items = new ArrayList<Alumno>();
//            for(int i=0; i<json.length();i++){
//                items.add(new Alumno(json.getJSONObject(i).getString("id_participante_evento"), json.getJSONObject(i).getString("nombres"), json.getJSONObject(i).getString("ape_pat")
//                        + " " + json.getJSONObject(i).getString("ape_mat"),false));
//            }
//        }catch(Exception e){}
//        return items;
//    }
//    public void cargarRecyclerView(ArrayList<Alumno> datos){
//
//        // Usar un administrador para LinearLayout
//        lManager = new LinearLayoutManager(this);
//
//        // Crear un nuevo adaptador
//        adapter = new AlumnoAdapter(datos);
//
//        // Obtener el Recycler
//        recycler = (RecyclerView) findViewById(R.id.reciclador);
//        recycler.setHasFixedSize(true);
//        recycler.setLayoutManager(lManager);
//        recycler.setAdapter(adapter);
//    }
//

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
        adapter = new AlumnoReporteAdapter(datos, getActivity().getApplication());

        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.recyclerview_reporte);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }

}
