package com.ricardo.controlasistenciaipd.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.Conexiones;
import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.adapters.AlumnoAsistenciaAdapter;
import com.ricardo.controlasistenciaipd.pojos.Alumno;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsistenciaActivity extends AppCompatActivity {

    private String host = Conexiones.host;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private TextView txtEvento, txtDocente, txtDisciplinaFecha, txtSinComplejos, txtSinHorarios, txtMensajeRecycler;
    private Spinner spHorarios, spComplejos;
    private Button btnContinuar;

    private String codPonente = "";
    private String codEvento = "";
    private String codHorario = "";
    private String codComplejo = "";

    private String recuperadoNomEvento = "";
    private String descripcionComplejo = "";
    private String descripcionDocente = "";
    private String descripcionDeporte = "";
    private String fechaHoy = "";
    private String descripcionHorario = "";

    private ArrayList<String> idComplejos = new ArrayList<String>();
    private ArrayList<String> idHorarios = new ArrayList<String>();
    private ArrayList<String> descComplejos = new ArrayList<String>();
    private ArrayList<String> descHorarios = new ArrayList<String>();
    private ArrayList<Alumno> items = new ArrayList<Alumno>();

    private Toolbar toolbar;

    public static Activity actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);
        actividad = this;
        final Bundle recupera=getIntent().getExtras();
        if(recupera != null){
            codPonente = recupera.getString("codigoPonente");
            codEvento = recupera.getString("codigoEvento");
            recuperadoNomEvento = recupera.getString("nombreEvento");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_asistencia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Control de Asistencia");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnContinuar = (Button) findViewById(R.id.btnContinuar);
        txtEvento = (TextView)findViewById(R.id.txt_asistencia_evento);
        txtEvento.setText(recuperadoNomEvento);
        txtDocente = (TextView)findViewById(R.id.txt_asistencia_docente);
        txtDisciplinaFecha = (TextView)findViewById(R.id.txt_asistencia_disciplina_fecha);
        txtSinComplejos = (TextView)findViewById(R.id.txt_no_complejos);
        txtSinHorarios = (TextView)findViewById(R.id.txt_no_horarios);
        txtMensajeRecycler = (TextView)findViewById(R.id.txt_mensaje_not_found);
        spComplejos = (Spinner)findViewById(R.id.sp_asistencia_complejos);
        spHorarios=(Spinner)findViewById(R.id.sp_asistencia_horarios);

        Thread thread = new Thread(){
            @Override
            public void run() {
                final String resDetalles = traerDetalles(codEvento, codPonente);
                final String resComplejos = traerComplejos(codEvento,codPonente);
                try {
                    JSONArray jsonArray = new JSONArray(resComplejos);
                    codComplejo = jsonArray.getJSONObject(0).getString("id_complejo");
                    descripcionComplejo = jsonArray.getJSONObject(0).getString("complejo");
                } catch (JSONException e) {}
                final String resHorarios = traerHorarios(codEvento,codComplejo,codPonente);
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(resHorarios);
                    codHorario = jsonArray.getJSONObject(0).getString("id_disciplinaevento");
                    descripcionHorario = jsonArray.getJSONObject(0).getString("horario");
                } catch (JSONException e) {}
                String resAlumnos = traerAlumnos(codEvento,codHorario);
                final ArrayList<Alumno> arregloAlumnos = ArregloLista(resAlumnos);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mostrarDetalles(resDetalles);
                        if(!codComplejo.isEmpty()) {
                            cargarSpiner(ArregloSpiner(resComplejos,0),0);
                            if(!codHorario.isEmpty()) {
                                cargarSpiner(ArregloSpiner(resHorarios,1),1);
                                if(!arregloAlumnos.isEmpty()) cargarRecyclerView(arregloAlumnos);
                                else{
                                    txtMensajeRecycler.setVisibility(View.VISIBLE);
                                    btnContinuar.setVisibility(View.INVISIBLE);
                                }
                            }else{
                                spHorarios.setVisibility(View.INVISIBLE);
                                txtMensajeRecycler.setVisibility(View.VISIBLE);
                                txtSinHorarios.setVisibility(View.VISIBLE);
                                txtMensajeRecycler.setText("No tiene horarios para este complejo");
                                btnContinuar.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            spComplejos.setVisibility(View.INVISIBLE);
                            txtMensajeRecycler.setVisibility(View.VISIBLE);
                            txtSinComplejos.setVisibility(View.VISIBLE);
                            txtMensajeRecycler.setText("No existen complejos asignados");
                            btnContinuar.setVisibility(View.INVISIBLE);
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
                        final String resHorarios = traerHorarios(codEvento, codComplejo,codPonente);
                        JSONArray jsonArray = null;
                        codHorario = "";
                        try {
                            jsonArray = new JSONArray(resHorarios);
                            codHorario = jsonArray.getJSONObject(0).getString("id_disciplinaevento");
                        } catch (JSONException e) {}
                        String resAlumnos = traerAlumnos(codEvento,codHorario);
                        final ArrayList<Alumno> arregloAlumnos = ArregloLista(resAlumnos);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spHorarios.setVisibility(View.VISIBLE);
                                txtMensajeRecycler.setVisibility(View.INVISIBLE);
                                btnContinuar.setVisibility(View.VISIBLE);
                                txtSinHorarios.setVisibility(View.INVISIBLE);
                                if(!codHorario.isEmpty()) {
                                    cargarSpiner(ArregloSpiner(resHorarios,1),1);
                                    if(!arregloAlumnos.isEmpty()) cargarRecyclerView(arregloAlumnos);
                                    else{
                                        txtMensajeRecycler.setVisibility(View.VISIBLE);
                                        btnContinuar.setVisibility(View.INVISIBLE);
                                    }
                                }else{
                                    spHorarios.setVisibility(View.INVISIBLE);
                                    txtMensajeRecycler.setVisibility(View.VISIBLE);
                                    txtSinHorarios.setVisibility(View.VISIBLE);
                                    txtMensajeRecycler.setText("No tiene horarios para este complejo");
                                    btnContinuar.setVisibility(View.INVISIBLE);
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

        spHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codHorario = idHorarios.get(position);
                descripcionHorario = descHorarios.get(position);
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        final String resAlumnos = traerAlumnos(codEvento,codHorario);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cargarRecyclerView(ArregloLista(resAlumnos));
                            }
                        });
                    }
                };
                tr.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
            url = new URL(host + "ListarHorarios.php?ide=" + codEve + "&cpj=" + codCom + "&cod=" + codDoc);
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
            url = new URL(host + "ListarAlumnos.php?ide=" + codEve + "&hor="+ codHor);
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
        try{
            String dDocente = "";
            String dDisciplina = "";
            String dFecha = "";
            JSONArray json = new JSONArray(response);
            for(int i = 0; i < json.length(); i++){
                dDocente = json.getJSONObject(i).getString("apepaterno") + " " + json.getJSONObject(i).getString("apematerno")
                        + " " + json.getJSONObject(i).getString("nombres");
                txtDocente.setText(dDocente);
                dDisciplina = json.getJSONObject(i).getString("curso");
                dFecha = json.getJSONObject(i).getString("fecha");
                txtDisciplinaFecha.setText(dDisciplina + "-" + dFecha);
            }
            descripcionDocente = dDocente;
            descripcionDeporte = dDisciplina;
            fechaHoy = dFecha;
        }catch(Exception e){}
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

    public void cargarSpiner(ArrayList<String> datos, int spinner){
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.custom_spinner,datos);
        if(spinner == 1) spHorarios.setAdapter(adaptador);
        else spComplejos.setAdapter(adaptador);
    }
    public ArrayList<Alumno> ArregloLista(String response){
        items = new ArrayList<Alumno>();
        try{
            JSONArray json=new JSONArray(response);
            String alumno = null;
            for(int i=0; i<json.length();i++){
                items.add(new Alumno(json.getJSONObject(i).getString("id_participante_evento"), json.getJSONObject(i).getString("nombres"), json.getJSONObject(i).getString("ape_pat")
                        + " " + json.getJSONObject(i).getString("ape_mat"),false));
            }
        }catch(Exception e){}
        return items;
    }
    public void cargarRecyclerView(ArrayList<Alumno> datos){
        lManager = new LinearLayoutManager(this);
        adapter = new AlumnoAsistenciaAdapter(datos);
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(lManager);
        recycler.setAdapter(adapter);
    }
    public void checkClick(View v){
        CheckBox alumno = (CheckBox) v.findViewById(R.id.cardViewAsistencia);
        LinearLayout alumnoLayout = (LinearLayout) alumno.getParent();
        TextView numero = (TextView) alumnoLayout.findViewById(R.id.cardviewNumero);
        int posicionAlumno = Integer.parseInt(String.valueOf(numero.getText()));
        boolean asistio = false;
        if(alumno.isChecked()) asistio = true;
        items.get(posicionAlumno-1).setAsistencia(asistio);
    }
    public void goConfirmar(View view){
        Intent intent = new Intent(this, ConfirmarActivity.class);
        intent.putExtra("codigoPonente",codPonente);
        intent.putExtra("codigoEvento", codEvento);
        intent.putExtra("nombreEvento",recuperadoNomEvento);
        intent.putExtra("nombreComplejo",descripcionComplejo);
        intent.putExtra("nombreDocente", descripcionDocente);
        intent.putExtra("nombreDisciplina",descripcionDeporte);
        intent.putExtra("nombreHorario",descripcionHorario);
        intent.putExtra("codigoHorario",codHorario);
        intent.putExtra("fecha",fechaHoy);
        intent.putExtra("alumnos", items);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_asistencia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_volver_menu) {
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            i.putExtra("codigoPonente", codPonente);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
