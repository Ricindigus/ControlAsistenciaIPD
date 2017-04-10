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

import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.adapters.DisciplinaAdapter;
import com.ricardo.controlasistenciaipd.pojos.Asistencia;
import com.ricardo.controlasistenciaipd.pojos.ReporteGeneral;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetalleAlumnoActivity extends AppCompatActivity {
    String codigoPonente = "", codigoAlumno = "", codigoEvento = "", disciplina1 = "", disciplina2 = "";
    TextView txtNombres, txtApellidos, txtDni, txtEdad, txtComplejo, txtDisciplina1, txtDisciplina2;
    int cantAsistencias1 = 0, cantFaltas1 = 0, cantAsistencias2 = 0, cantFaltas2 = 0;
    ImageView imgAlumno1, imgAlumno2;
    RecyclerView recyclerDisciplina1, recyclerDisciplina2;
    LinearLayoutManager lManager;
    DisciplinaAdapter adapter;
    ArrayList<Asistencia> asistencias1, asistencias2;
    private String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";
    //String hostIpdProduccion = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    //String hostlocal = "http://10.10.118.16//WebServiceAndroid/";
    //String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reporte_alumno);
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

//        items = new ArrayList<Asistencia>();
//        items.add(new Asistencia("01/07/2017","A"));
//        items.add(new Asistencia("08/07/2017","A"));
//        items.add(new Asistencia("15/07/2017","A"));
//        items.add(new Asistencia("22/07/2017","F"));
//        items.add(new Asistencia("29/07/2017","A"));
//        items.add(new Asistencia("01/08/2017","A"));
//        items.add(new Asistencia("08/08/2017","A"));
//        items.add(new Asistencia("15/082017","A"));
//        items.add(new Asistencia("22/08/2017","A"));
//        items.add(new Asistencia("29/08/2017","A"));
//        items.add(new Asistencia("01/09/2017","A"));
//        items.add(new Asistencia("08/09/2017","A"));
//        items.add(new Asistencia("15/09/2017","A"));
//        items.add(new Asistencia("22/09/2017","A"));
//        items.add(new Asistencia("29/09/2017","A"));
//        items.add(new Asistencia("01/10/2017","A"));
//        items.add(new Asistencia("08/10/2017","F"));
//        items.add(new Asistencia("15/10/2017","A"));
//        items.add(new Asistencia("22/10/2017","A"));
//        items.add(new Asistencia("29/10/2017","A"));
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
                       txtDisciplina2.setText(disciplina2 + " (Asistió:"+ cantAsistencias2 + "/ Faltó:"+cantFaltas2+")");
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asistencia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            url = new URL(hostIpdDesarrollo + "DatosPersonalesAlumno.php?alu=" + codAlu);
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
    public void mostrarDatosAlumno(String response){
        try{
            String dDocente = "";
            String dDisciplina = "";
            JSONArray json = new JSONArray(response);
            for(int i = 0; i < json.length(); i++){
                txtNombres.setText(json.getJSONObject(i).getString("nombres").toUpperCase());
                txtApellidos.setText(json.getJSONObject(i).getString("apepaterno").toUpperCase()
                        + " " + json.getJSONObject(i).getString("apematerno").toUpperCase());
                txtDni.setText(json.getJSONObject(i).getString("dni"));
                txtEdad.setText(json.getJSONObject(i).getString("edad") + " años");
                if(!json.getJSONObject(i).getBoolean("sexo")){
                    imgAlumno1.setVisibility(View.INVISIBLE);
                    imgAlumno2.setVisibility(View.VISIBLE);
                }
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
            url = new URL(hostIpdDesarrollo + "AsistenciaTotalAlumno.php?eve="+ codEve +"&alu="+ codAlu);
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
        adapter= new DisciplinaAdapter(datos);
        r.setAdapter(adapter);
    }
}
