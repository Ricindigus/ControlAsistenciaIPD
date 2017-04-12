package com.ricardo.controlasistenciaipd.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.Conexiones;
import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.pojos.Alumno;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GuardarActivity extends AppCompatActivity {
    private String host = Conexiones.host;
    private int codigoError = 0;
    private String codigoPonente="";
    private String codigoEvento = "";
    private String codigoHorario = "";
    private String nombreEvento = "";
    private EditText txtObservacion;
    private ArrayList<Alumno> asistenciaAlumnos;
    private String fechaHoy ="";
    private TextView txtFecha;
    private Toolbar toolbar;
    public static Activity actividad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar);

        asistenciaAlumnos = new ArrayList<Alumno>();
        Bundle recupera = getIntent().getExtras();
        actividad = this;
        if(recupera != null){
            asistenciaAlumnos = (ArrayList<Alumno>)recupera.getSerializable("alumnos");
            codigoPonente = recupera.getString("codigoPonente");
            codigoEvento = recupera.getString("codigoEvento");
            codigoHorario = recupera.getString("codigoHorario");
            nombreEvento = recupera.getString("nombreEvento");
            fechaHoy = recupera.getString("fecha");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_asistencia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Guardar Asistencia");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtObservacion = (EditText) findViewById(R.id.txtObservaciones);
        txtFecha = (TextView)findViewById(R.id.txtFecha);
        txtFecha.setText(fechaHoy);
    }

    public void goFinal(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro de grabar los datos ingresados?")
                .setTitle("Aviso")
                .setCancelable(false)
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                goFin(view);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void goFin(View view){
        Thread tr2=new Thread(){
            @Override
            public void run() {
                boolean incorrecto = false;
                int i=0;
                while(i < asistenciaAlumnos.size() && !incorrecto){
                    String resultado=tomarAsistencia(asistenciaAlumnos.get(i).getCodigo(),codigoPonente,asistenciaAlumnos.get(i).getAsistencia());
                    try{
                        JSONArray json=new JSONArray(resultado);
                        int estado = Integer.parseInt(json.getJSONObject(0).getString("estado_final"));
                        if(estado == 2) incorrecto = true;
                    }catch(Exception e){}
                    i++;
                }
                if(incorrecto){ codigoError = 1;}
                else{
                    String resultado = registrarObservacion(codigoHorario, codigoPonente,txtObservacion.getText().toString());
                    try{
                        JSONArray json = new JSONArray(resultado);
                        int estado = Integer.parseInt(json.getJSONObject(0).getString("estado_final"));
                        if(estado == 2) {
                            codigoError = 1;
                        }else{
                            //actualizar
//                            String resultado2 = actualizarEstado(codigoHorario);
                        }
                    }catch(Exception e){}
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i=new Intent(getApplicationContext(),FinalizarActivity.class);
                        i.putExtra("codigoError",codigoError);
                        i.putExtra("codigoPonente",codigoPonente);
                        i.putExtra("codigoEvento",codigoEvento);
                        i.putExtra("nombreEvento",nombreEvento);
                        startActivity(i);
                    }
                });
            }
        };
        tr2.start();
    }

    public String tomarAsistencia(String codAlumno, String codPonente, boolean asistencia){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(host + "tomarasistencia.php?alu="+codAlumno+"&pon="+codPonente+"&asi="+asistencia);
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
    public String registrarObservacion(String codHorario, String codPonente, String observacion){
        URL url=null;
        observacion = observacion.replace(" ","%20");
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(host + "registrarobservacion.php?hor=" + codHorario + "&pon=" + codPonente + "&obs=" + observacion);
            HttpURLConnection conection = (HttpURLConnection)url.openConnection();
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
//    public String actualizarEstado(String codHorario){
//        URL url=null;
//        String linea="";
//        int respuesta=0;
//        StringBuilder resul=null;
//        try{
//            url = new URL(hostIpdDesarrollo + "actualizarestado.php?hor=" + codHorario);
//            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
//            respuesta=conection.getResponseCode();
//            resul=new StringBuilder();
//            if(respuesta==HttpURLConnection.HTTP_OK){
//                InputStream in=new BufferedInputStream(conection.getInputStream());
//                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
//                while((linea=reader.readLine())!=null){
//                    resul.append(linea);
//                }
//            }
//        }catch(Exception e){}
//        return resul.toString();
//    }

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
}
