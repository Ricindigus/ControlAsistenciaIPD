package com.ricardo.controlasistenciaipd.fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Date;


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ricardo.controlasistenciaipd.pojos.Alumno;
import com.ricardo.controlasistenciaipd.pojos.Asistencia;
import com.ricardo.controlasistenciaipd.pojos.ReporteAlumno;
import com.ricardo.controlasistenciaipd.adapters.AlumnoReporteAdapter;
import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.pojos.ReporteGeneral;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralFragment extends Fragment {
    private String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";
    //String hostIpdProduccion = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    //String hostlocal = "http://10.10.118.16//WebServiceAndroid/";
    //String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";

    private int dia1, mes1, anio1;
    private int dia2, mes2, anio2;
    private TextView txtFecha1, txtFecha2, txtEvento, txtDocDisciplina;
    private Spinner spHorarios, spComplejos;
    private ArrayList<String> complejos, horarios, idComplejos, idHorarios, descComplejos, descHorarios;
    private ArrayList<ReporteGeneral> alumnos;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private View view;
    private String codDocente = "", codEvento =  "", nomEvento = "", codComplejo = "", descripcionComplejo = "",
            codHorario = "", descripcionHorario = "", fechaInicio = "", fechaFin = "", codDisciplina = "";
    private Button btnBuscar;

    public GeneralFragment(){}

    @SuppressLint("ValidFragment")
    public GeneralFragment(Bundle recupera) {
        // Required empty public constructor
        final Bundle r = recupera;
        if(r!=null){
            codDocente = recupera.getString("codigoPonente");
            codEvento = recupera.getString("codigoEvento");
            nomEvento = recupera.getString("nombreEvento");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_general, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarDatos();
        spHorarios = (Spinner)view.findViewById(R.id.sp_reporte_horario);
        spComplejos = (Spinner)view.findViewById(R.id.sp_reporte_complejo);
        txtFecha1 = (TextView)view.findViewById(R.id.txtFecha1);
        txtFecha2 = (TextView)view.findViewById(R.id.txtFecha2);
        txtEvento = (TextView)view.findViewById(R.id.txt_reportes_evento);
        txtDocDisciplina = (TextView)view.findViewById(R.id.txt_reportes_docente_disciplina);
        btnBuscar = (Button)view.findViewById(R.id.btnBuscarRepotes);
        recycler = (RecyclerView) view.findViewById(R.id.recyclerview_reporte);

//        cargarSpiner(complejos, 0);
//        cargarSpiner(horarios, 1);

        Calendar calendar1 = Calendar.getInstance();
        dia1 = dia2 = calendar1.get(Calendar.DAY_OF_MONTH);
        mes1 = mes2 = calendar1.get(Calendar.MONTH);
        anio1 = anio2 = calendar1.get(Calendar.YEAR);

        String mAux = "" + (mes1 + 1);
        if(mes1 < 10) mAux = "0" + mAux;
        String dAux = "" + dia1;
        if(dia1 < 10) dAux = "0" + dAux;



        txtFecha1.setText(dAux + "/" + mAux + "/" + anio1);
        txtFecha2.setText(dAux + "/" + mAux + "/" + anio2);

        fechaInicio = anio1 + "-" + mAux + "-" + dAux + "%2000:00:00";
        fechaFin = anio2 + "-" + mAux + "-" + dAux + "%2023:59:59";

        Thread thread = new Thread(){
            @Override
            public void run() {
                final String resDetalles = traerDetalles(codEvento, codDocente);
                final String resComplejos = traerComplejos(codEvento, codDocente);
                try {
                    JSONArray jsonArray = new JSONArray(resComplejos);
                    codComplejo = jsonArray.getJSONObject(0).getString("id_complejo");
                    descripcionComplejo = jsonArray.getJSONObject(0).getString("complejo");
                } catch (JSONException e) {}
                final String resHorarios = traerHorarios(codEvento, codComplejo, codDocente);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(resHorarios);
                    codHorario = jsonArray.getJSONObject(0).getString("id_disciplinaevento");
                    descripcionHorario = jsonArray.getJSONObject(0).getString("horario");
                } catch (JSONException e) {}
                String resAlumnos = traerAlumnos(codEvento,codHorario);
                final ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                final String resReportes = traerReportes(codEvento, codDocente, fechaInicio, fechaFin, codComplejo, codHorario);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mostrarDetalles(resDetalles);
                        cargarSpiner(ArregloSpiner(resComplejos,0),0);
                        cargarSpiner(ArregloSpiner(resHorarios,1),1);
                        //cargarRecyclerView(ArregloReportes(listaAlumnos, resReportes));
                    }
                });
            }
        };
        thread.start();

        spComplejos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codComplejo = idComplejos.get(position);
                descripcionComplejo = descComplejos.get(position);
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        final String resHorarios = traerHorarios(codEvento, codComplejo,codDocente);
                        JSONArray jsonArray = null;
                        codHorario = "";
                        try {
                            jsonArray = new JSONArray(resHorarios);
                            codHorario = jsonArray.getJSONObject(0).getString("id_disciplinaevento");
                        } catch (JSONException e) {}
                        String resAlumnos = traerAlumnos(codEvento,codHorario);
                        final ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                        final String resReportes = traerReportes(codEvento, codDocente, fechaInicio, fechaFin, codComplejo, codHorario);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cargarSpiner(ArregloSpiner(resHorarios,1),1);
                                //cargarRecyclerView(ArregloReportes(listaAlumnos, resReportes));
                            }
                        });
                    }
                };
                tr.start();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codHorario = idHorarios.get(position);
                descripcionHorario = descHorarios.get(position);
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        String resAlumnos = traerAlumnos(codEvento,codHorario);
                        final ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                        final String resReportes = traerReportes(codEvento, codDocente, fechaInicio, fechaFin, codComplejo, codHorario);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //cargarRecyclerView(ArregloReportes(listaAlumnos, resReportes));
                            }
                        });
                    }
                };
                tr.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        String resAlumnos = traerAlumnos(codEvento,codHorario);
                        final ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                        final String resReportes = traerReportes(codEvento, codDocente, fechaInicio, fechaFin, codComplejo, codHorario);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cargarRecyclerView(ArregloReportes(listaAlumnos, resReportes));
                            }
                        });
                    }
                };
                tr.start();
            }
        });

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
                        fechaInicio = year + "-" + m + "-" + d + "%2000:00:00";
                        anio1 = year;
                        mes1 = month-1;
                        dia1 = dayOfMonth;
                    }
                },anio1,mes1,dia1);
                Calendar c = Calendar.getInstance();
                c.set(anio2,mes2,dia2);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
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
                        fechaFin = year + "-" + m + "-" + d + "%2023:59:59";
                        anio2 = year;
                        mes2 = month-1;
                        dia2 = dayOfMonth;
                    }
                },anio2,mes2,dia2);
                Calendar c = Calendar.getInstance();
                c.set(anio1,mes1,dia1);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        cargarRecyclerView(alumnos);
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
    //METODO QUE PERMITE MOSTRAR EL NOMBRE
    public void mostrarDetalles(String response){
        try{
            String dDocente = "";
            String dDisciplina = "";
            JSONArray json = new JSONArray(response);
            for(int i = 0; i < json.length(); i++){
                dDocente = json.getJSONObject(i).getString("nombres") + " " +json.getJSONObject(i).getString("apepaterno");
                dDisciplina = json.getJSONObject(i).getString("curso");
                txtDocDisciplina.setText(dDocente + "-" + dDisciplina);
            }
        }catch(Exception e){}
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
            url = new URL(hostIpdDesarrollo + "ListarHorariosReporte.php?eve=" + codEve + "&cpj=" + codCom + "&doc=" + codDoc);
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


    public String traerAlumnos(String codEve, String codHor){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "ListarAlumnosReporte.php?ide=" + codEve + "&hor="+ codHor);
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

    public String traerReportes(String codEve, String codDoc, String inicio, String fin, String codCom, String codHor){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "ListarReportes.php?ide="+ codEve +
                    "&doc="+ codDoc +
                    "&fi="+ inicio +
                    "&ff="+ fin +
                    "&com=" + codCom +
                    "&hor=" + codHor);
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

    public ArrayList<String> ArregloSpiner(String response, int spinner){
        ArrayList<String> listado=new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        if(spinner == 1){
            try{
                JSONArray json=new JSONArray(response);
                String texto="";
                for(int i=0; i<json.length();i++){
                    codHorario = json.getJSONObject(i).getString("id_disciplinaevento");
                    ids.add(codHorario);
                    texto=json.getJSONObject(i).getString("horario");
                    listado.add(texto);
                }
                idHorarios = ids;
                descHorarios = listado;
            }catch(Exception e){}
        }else{
            try{
                JSONArray json=new JSONArray(response);
                String texto="";
                for(int i=0; i<json.length();i++){
                    codComplejo = json.getJSONObject(i).getString("id_complejo");
                    ids.add(codComplejo);
                    texto=json.getJSONObject(i).getString("complejo");
                    listado.add(texto);
                }
                idComplejos = ids;
                descComplejos = listado;
            }catch(Exception e){}
        }
        return listado;
    }

    public ArrayList<ReporteGeneral> ArregloLista(String response){
        alumnos = new ArrayList<ReporteGeneral>();
        try{
            JSONArray json=new JSONArray(response);
            for(int i=0; i<json.length();i++){
                alumnos.add(new ReporteGeneral(json.getJSONObject(i).getString("id_participante_evento"),
                        json.getJSONObject(i).getString("nombres"),
                        json.getJSONObject(i).getString("ape_pat") + " " + json.getJSONObject(i).getString("ape_mat"),
                        null));
            }
        }catch(Exception e){}
        return alumnos;
    }

    public ArrayList<ReporteGeneral> ArregloReportes(ArrayList<ReporteGeneral> arrayAlumnos, String response){
        ArrayList<ReporteGeneral> arrayAux = new ArrayList<ReporteGeneral>();
        String codigoAlumno = "";
        try{
            JSONArray json=new JSONArray(response);
            for (int i = 0; i <arrayAlumnos.size(); i++) {
                codigoAlumno = arrayAlumnos.get(i).getCodigo();
                ArrayList<Asistencia> asistencias = new ArrayList<Asistencia>();
                for(int j=0; j<json.length();j++){
                    if(json.getJSONObject(j).getString("codigo").equals(codigoAlumno))
                        asistencias.add(new Asistencia(json.getJSONObject(j).getString("fecha"),json.getJSONObject(j).getString("asistencia")));
                }
                arrayAux.add(new ReporteGeneral(codigoAlumno,arrayAlumnos.get(i).getNombres(),arrayAlumnos.get(i).getApellidos(),asistencias));
            }
        }catch(Exception e){}
        return arrayAux;
    }

    public void cargarSpiner(ArrayList<String> datos, int spinner){
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(getContext(),R.layout.custom_spinner,datos);
        if(spinner == 0)
            spComplejos.setAdapter(adaptador);
        else
            spHorarios.setAdapter(adaptador);
    }

    public void cargarRecyclerView(ArrayList<ReporteGeneral> datos){

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());

        // Crear un nuevo adaptador
        adapter = new AlumnoReporteAdapter(datos, getActivity().getApplication());

        // Obtener el Recycler
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }
    public void iniciarDatos(){
        horarios = new ArrayList<String>();
        complejos = new ArrayList<String>();
        alumnos = new ArrayList<ReporteGeneral>();
        idComplejos = new ArrayList<String>();
        idHorarios = new ArrayList<String>();
        descComplejos = new ArrayList<String>();
        descHorarios = new ArrayList<String>();

//        horarios.add("LUN 14:00-15:00,MIE 14:00-15:00,VIE 14:00-15:00,SAB 14:00-15:00");
//        horarios.add("LUN 15:00-16:00,MIE 15:00-16:00,VIE 15:00-16:00");
//        horarios.add("LUN 16:00-17:00,MIE 16:00-17:00,VIE 16:00-17:00");
//        horarios.add("MAR/JUE 14:00-15:00");
//        horarios.add("MAR/JUE 15:00-16:00");
//        horarios.add("MAR/JUE 16:00-17:00");
//        horarios.add("SAB 08:00-09:00");
//        horarios.add("SAB 09:00-10:00");
//        horarios.add("SAB 10:00-11:00");
//
//        complejos.add("ESTADIO NACIONAL");
//        complejos.add("ESTADIO DELLE ALPHI");
//        complejos.add("ESTADIO CAMP NOU");
//        complejos.add("EMIRATES STADIUM");
//        complejos.add("ESTADIO SANTIAGO BERNABEU");

//        boolean[] arrayAsistencias = {true,true,true,false,true,false,true,true,true};
//        alumnos.add(new ReporteAlumno("Denis Ricardo","Morales Retamozo",6,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Alan Arnold","Ramos Gonzales",7,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Jesus Jose","Falen Pacheco",6,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Enrique Julio","Flores Dibala",7,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Laura Rosa Sosa","Villanueva",6,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Cesar Bernabe","Quispe Rodriguez",7,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Helton Javier","Aiquipa Robles",6,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Cindy Laura","Maldonado Quispe",7,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Nilton Gregorio","Armas Domenech",6,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Juan Pablo","Messi Nazario",7,arrayAsistencias));
//        alumnos.add(new ReporteAlumno("Guiliana Lisa","Montes Fajardo",6,arrayAsistencias));
    }

}
