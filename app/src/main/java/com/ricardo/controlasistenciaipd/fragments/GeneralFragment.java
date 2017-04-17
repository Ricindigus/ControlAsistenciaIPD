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


import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ricardo.controlasistenciaipd.Conexiones;
import com.ricardo.controlasistenciaipd.pojos.Asistencia;
import com.ricardo.controlasistenciaipd.adapters.AlumnoReporteAdapter;
import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.pojos.ReporteGeneral;

import org.json.JSONArray;
import org.json.JSONException;


public class GeneralFragment extends Fragment {
    private String host = Conexiones.host;

    private int dia1, mes1, anio1;
    private int dia2, mes2, anio2;
    private TextView txtEvento, txtDocDisciplina, txtMensaje, txtSinComplejos, txtSinHorarios;
    private Button btnFecha1, btnFecha2;
    private Spinner spHorarios, spComplejos;
    private ArrayList<String> idComplejos, idHorarios, descComplejos, descHorarios;
    private ArrayList<ReporteGeneral> alumnos;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private View view;
    private String codigoDocente = "", codigoEvento =  "", nombreEvento = "", codComplejo = "", descripcionComplejo = "",
            codHorario = "", descripcionHorario = "", fechaInicio = "", fechaFin = "", codDisciplina = "";

    public GeneralFragment(){}

    @SuppressLint("ValidFragment")
    public GeneralFragment(Bundle recupera) {
        final Bundle r = recupera;
        if(r!=null){
            codigoDocente = recupera.getString("codigoPonente");
            codigoEvento = recupera.getString("codigoEvento");
            nombreEvento = recupera.getString("nombreEvento");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciarDatos();
        spHorarios = (Spinner)view.findViewById(R.id.sp_reporte_horario);
        spComplejos = (Spinner)view.findViewById(R.id.sp_reporte_complejo);
        btnFecha1 = (Button)view.findViewById(R.id.btnFecha1);
        btnFecha2 = (Button)view.findViewById(R.id.btnFecha2);
        txtEvento = (TextView)view.findViewById(R.id.txt_reportes_evento);
        txtDocDisciplina = (TextView)view.findViewById(R.id.txt_reportes_docente_disciplina);
        txtMensaje = (TextView)view.findViewById(R.id.txt_mensaje);
        txtSinComplejos = (TextView)view.findViewById(R.id.txt_sin_complejos);
        txtSinHorarios = (TextView)view.findViewById(R.id.txt_sin_horarios);
        recycler = (RecyclerView) view.findViewById(R.id.recyclerview_reporte);

        Calendar calendar1 = Calendar.getInstance();
        dia1 = 1;
        dia2 = calendar1.get(Calendar.DAY_OF_MONTH);
        mes1 = mes2 = calendar1.get(Calendar.MONTH);
        anio1 = anio2 = calendar1.get(Calendar.YEAR);

        String mAux = "" + (mes2 + 1);
        if(mes2< 10) mAux = "0" + mAux;
        String dAux = "" + dia2;
        if(dia2 < 10) dAux = "0" + dAux;

        btnFecha1.setText("01" + "/" + mAux + "/" + anio1);
        btnFecha2.setText(dAux + "/" + mAux + "/" + anio2);

        fechaInicio = anio1 + "-" + mAux + "-" + "01" + "%2000:00:00";
        fechaFin = anio2 + "-" + mAux + "-" + dAux + "%2023:59:59";

        Thread thread = new Thread(){
            @Override
            public void run() {
                final String resDetalles = traerDetalles(codigoEvento, codigoDocente);
                final String resComplejos = traerComplejos(codigoEvento, codigoDocente);
                try {
                    JSONArray jsonArray = new JSONArray(resComplejos);
                    codComplejo = jsonArray.getJSONObject(0).getString("id_complejo");
                    descripcionComplejo = jsonArray.getJSONObject(0).getString("complejo");
                } catch (JSONException e) {}
                final String resHorarios = traerHorarios(codigoEvento, codComplejo, codigoDocente);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(resHorarios);
                    codHorario = jsonArray.getJSONObject(0).getString("id_disciplinaevento");
                    descripcionHorario = jsonArray.getJSONObject(0).getString("horario");
                } catch (JSONException e) {}
                String resAlumnos = traerAlumnos(codigoEvento,codHorario);
                String resReportes = traerReportes(codigoEvento, codigoDocente, fechaInicio, fechaFin, codComplejo, codHorario);
                ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                final ArrayList<ReporteGeneral> arregloReportes = ArregloReportes(listaAlumnos, resReportes);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mostrarDetalles(resDetalles);
                        if(!codComplejo.isEmpty()) {
                            cargarSpiner(ArregloSpiner(resComplejos,0),0);
                            if(!codHorario.isEmpty()) {
                                cargarSpiner(ArregloSpiner(resHorarios,1),1);
                                if(!arregloReportes.isEmpty()) cargarRecyclerView(arregloReportes);
                                else txtMensaje.setVisibility(View.VISIBLE);
                            }else{
                                spHorarios.setVisibility(View.INVISIBLE);
                                txtSinHorarios.setVisibility(View.VISIBLE);
                                txtMensaje.setText("No tiene horarios para este complejo");
                                txtMensaje.setVisibility(View.VISIBLE);
                            }
                        }else{
                            spComplejos.setVisibility(View.INVISIBLE);
                            txtSinComplejos.setVisibility(View.VISIBLE);
                            txtMensaje.setText("No existen complejos asignados");
                            txtMensaje.setVisibility(View.VISIBLE);
                        }
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
                        final String resHorarios = traerHorarios(codigoEvento, codComplejo, codigoDocente);
                        JSONArray jsonArray = null;
                        codHorario = "";
                        try {
                            jsonArray = new JSONArray(resHorarios);
                            codHorario = jsonArray.getJSONObject(0).getString("id_disciplinaevento");
                        } catch (JSONException e) {}
                        String resAlumnos = traerAlumnos(codigoEvento,codHorario);
                        String resReportes = traerReportes(codigoEvento, codigoDocente, fechaInicio, fechaFin, codComplejo, codHorario);
                        ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                        final ArrayList<ReporteGeneral> arregloReportes = ArregloReportes(listaAlumnos, resReportes);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtSinHorarios.setVisibility(View.INVISIBLE);
                                spHorarios.setVisibility(View.VISIBLE);
                                txtMensaje.setVisibility(View.INVISIBLE);
                                if(!codHorario.isEmpty()) {
                                    cargarSpiner(ArregloSpiner(resHorarios,1),1);
                                    if(!arregloReportes.isEmpty()) cargarRecyclerView(arregloReportes);
                                    else{
                                        txtMensaje.setText("No hay alumnos para este horario");
                                        txtMensaje.setVisibility(View.VISIBLE);
                                    }
                                }else{
                                    spHorarios.setVisibility(View.INVISIBLE);
                                    txtSinHorarios.setVisibility(View.VISIBLE);
                                    txtMensaje.setText("No tiene horarios para este complejo");
                                    txtMensaje.setVisibility(View.VISIBLE);
                                }
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
                        String resAlumnos = traerAlumnos(codigoEvento,codHorario);
                        ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                        String resReportes = traerReportes(codigoEvento, codigoDocente, fechaInicio, fechaFin, codComplejo, codHorario);
                        final ArrayList<ReporteGeneral> arregloReportes = ArregloReportes(listaAlumnos, resReportes);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtMensaje.setVisibility(View.INVISIBLE);
                                if(!arregloReportes.isEmpty()) cargarRecyclerView(arregloReportes);
                                else{
                                    txtMensaje.setText("No hay alumnos para este horario");
                                    txtMensaje.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                };
                tr.start();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        btnFecha1.setOnClickListener(new View.OnClickListener() {
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
                        btnFecha1.setText(d + "/" + m + "/" + year);
                        fechaInicio = year + "-" + m + "-" + d + "%2000:00:00";
                        anio1 = year;
                        mes1 = month-1;
                        dia1 = dayOfMonth;
                        Thread tr = new Thread() {
                            @Override
                            public void run() {
                                String resAlumnos = traerAlumnos(codigoEvento,codHorario);
                                final ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                                final String resReportes = traerReportes(codigoEvento, codigoDocente, fechaInicio, fechaFin, codComplejo, codHorario);
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
                },anio1,mes1,dia1);
                Calendar c = Calendar.getInstance();
                c.set(anio2,mes2,dia2);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();

            }
        });
        btnFecha2.setOnClickListener(new View.OnClickListener() {
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
                        btnFecha2.setText(d + "/" + m + "/" + year);
                        fechaFin = year + "-" + m + "-" + d + "%2023:59:59";
                        anio2 = year;
                        mes2 = month-1;
                        dia2 = dayOfMonth;
                        Thread tr = new Thread() {
                            @Override
                            public void run() {
                                String resAlumnos = traerAlumnos(codigoEvento,codHorario);
                                final ArrayList<ReporteGeneral> listaAlumnos = ArregloLista(resAlumnos);
                                final String resReportes = traerReportes(codigoEvento, codigoDocente, fechaInicio, fechaFin, codComplejo, codHorario);
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
            url = new URL(host + "ListarDetalle.php?ide=" + codEve + "&cod=" + codDoc);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null) resul.append(linea);
            }
        }catch(Exception e){}
        return resul.toString();
    }

    public void mostrarDetalles(String response){
        txtEvento.setText(nombreEvento);
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
            url = new URL(host + "ListarComplejos.php?ide="+codigoEve+"&cod="+codDoc);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null) resul.append(linea);
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
            url = new URL(host + "ListarHorariosReporte.php?eve=" + codEve + "&cpj=" + codCom + "&doc=" + codDoc);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null) resul.append(linea);
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
            url = new URL(host + "ListarAlumnosReporte.php?ide=" + codEve + "&hor="+ codHor);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null) resul.append(linea);
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
            url = new URL(host + "ListarReportes.php?ide="+ codEve +
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
                while((linea=reader.readLine())!=null) resul.append(linea);
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
        lManager = new LinearLayoutManager(getActivity());
        adapter = new AlumnoReporteAdapter(datos, getActivity().getApplication());
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }
    public void iniciarDatos(){
        alumnos = new ArrayList<ReporteGeneral>();
        idComplejos = new ArrayList<String>();
        idHorarios = new ArrayList<String>();
        descComplejos = new ArrayList<String>();
        descHorarios = new ArrayList<String>();
    }

}
