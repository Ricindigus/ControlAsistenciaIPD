package com.ricardo.controlasistenciaipd.activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.Conexiones;
import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.adapters.AsistenciaDisciplinaAdapter;
import com.ricardo.controlasistenciaipd.pojos.Asistencia;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetalleAlumnoActivity extends AppCompatActivity {
    private String codigoPonente = "", codigoAlumno = "", codigoEvento = "", disciplina1 = "", disciplina2 = "";
    private TextView txtNombres, txtApellidos, txtDni, txtEdad, txtComplejo, txtDisciplina1, txtDisciplina2;
    private int cantAsistencias1 = 0, cantFaltas1 = 0, cantAsistencias2 = 0, cantFaltas2 = 0;
    private ImageView imgAlumno1, imgAlumno2;
    private RecyclerView recyclerDisciplina1, recyclerDisciplina2;
    private LinearLayoutManager lManager;
    private AsistenciaDisciplinaAdapter adapter;
    private ArrayList<Asistencia> asistencias1, asistencias2;
    private String host = Conexiones.host;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alumno);
        Bundle recupera = getIntent().getExtras();
        if(recupera != null){
            codigoPonente = recupera.getString("codigoPonente");
            codigoAlumno = recupera.getString("codigoAlumno");
            codigoEvento = recupera.getString("codigoEvento");
        }

        txtNombres = (TextView)findViewById(R.id.txt_reporte_alumno_nombres);
        txtApellidos = (TextView)findViewById(R.id.txt_reporte_alumno_apellidos);
        txtDni = (TextView)findViewById(R.id.txt_reporte_alumno_dni);
        txtEdad = (TextView)findViewById(R.id.txt_reporte_alumno_edad);
        txtComplejo = (TextView)findViewById(R.id.txt_reporte_alumno_complejo);
        txtDisciplina1 = (TextView)findViewById(R.id.txt_reporte_alumno_disciplina1);
        txtDisciplina2 = (TextView)findViewById(R.id.txt_reporte_alumno_disciplina2);
        imgAlumno1 = (ImageView)findViewById(R.id.img_reporte_alumno_boy);
        imgAlumno2 = (ImageView)findViewById(R.id.img_reporte_alumno_girl);
        recyclerDisciplina1 = (RecyclerView)findViewById(R.id.recycler_disciplina_1);
        recyclerDisciplina2 = (RecyclerView)findViewById(R.id.recycler_disciplina_2);
        toolbar = (Toolbar) findViewById(R.id.toolbar_reporte_alumno);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reporte del Alumno");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Thread peticionDatos = new Thread(){
            @Override
            public void run() {
                final String resDatosPersonales = traerDatosAlumnos(codigoAlumno);
                String resAsistencia = traerAsistencias(codigoEvento,codigoAlumno);
                ArregloAsistencias(resAsistencia);
                runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mostrarDatosAlumno(resDatosPersonales);
                       txtDisciplina1.setText(disciplina1 + " (Asistió:"+ cantAsistencias1 + "/ Faltó:"+cantFaltas1+")");
                       if(!disciplina2.isEmpty()) txtDisciplina2.setText(disciplina2 + " (Asistió:"+ cantAsistencias2 + "/ Faltó:"+cantFaltas2+")");
                       cargarRecycler(1,asistencias1);
                       cargarRecycler(2,asistencias2);
                   }
                });
            }
        };
        peticionDatos.start();
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
            i.putExtra("codigoPonente", codigoPonente);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public String traerDatosAlumnos(String codAlu){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(host + "DatosPersonalesAlumno.php?alu=" + codAlu);
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
    public void mostrarDatosAlumno(String response){
        try{
            JSONArray json = new JSONArray(response);
            for(int i = 0; i < json.length(); i++){
                txtNombres.setText(json.getJSONObject(i).getString("nombres").toUpperCase());
                txtApellidos.setText(json.getJSONObject(i).getString("apepaterno").toUpperCase()
                        + " " + json.getJSONObject(i).getString("apematerno").toUpperCase());
                txtDni.setText(json.getJSONObject(i).getString("dni"));
                txtEdad.setText(json.getJSONObject(i).getString("edad") + " años");
                if(json.getJSONObject(i).getBoolean("sexo")) imgAlumno1.setVisibility(View.VISIBLE);
                else imgAlumno2.setVisibility(View.VISIBLE);
                txtComplejo.setText(json.getJSONObject(i).getString("complejo"));
            }
        }catch(Exception e){}
    }

    public String traerAsistencias(String codEve, String codAlu){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(host+ "AsistenciaTotalAlumno.php?eve="+ codEve +"&alu="+ codAlu);
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
    public void ArregloAsistencias(String response){
        asistencias1 = new ArrayList<Asistencia>();
        asistencias2 = new ArrayList<Asistencia>();
        try{
            JSONArray json = new JSONArray(response);
            disciplina1 = json.getJSONObject(0).getString("disciplina").toUpperCase();
            boolean encontrado =  false;
            json=new JSONArray(response);
            for(int j=0; j<json.length();j++){
                if(json.getJSONObject(j).getString("disciplina").toUpperCase().equals(disciplina1)) {
                    asistencias1.add(new Asistencia(json.getJSONObject(j).getString("fecha"), json.getJSONObject(j).getString("asistencia")));
                    if(json.getJSONObject(j).getString("asistencia").equals("A")) cantAsistencias1++;
                    else cantFaltas1++;
                }else {
                    if(!encontrado){
                        disciplina2 = json.getJSONObject(j).getString("disciplina").toUpperCase();
                        encontrado = true;
                    }
                    asistencias2.add(new Asistencia(json.getJSONObject(j).getString("fecha"), json.getJSONObject(j).getString("asistencia")));
                    if(json.getJSONObject(j).getString("asistencia").equals("A")) cantAsistencias2++;
                    else cantFaltas2++;
                }
            }
        }catch(Exception e){}
    }
    public void cargarRecycler(int recyclerACargar, ArrayList<Asistencia> datos){
        RecyclerView r;
        if(recyclerACargar == 1) r = recyclerDisciplina1;
        else r = recyclerDisciplina2;
        r.setHasFixedSize(true);
        lManager = new LinearLayoutManager(this);
        r.setLayoutManager(lManager);
        adapter= new AsistenciaDisciplinaAdapter(datos);
        r.setAdapter(adapter);
    }
}
