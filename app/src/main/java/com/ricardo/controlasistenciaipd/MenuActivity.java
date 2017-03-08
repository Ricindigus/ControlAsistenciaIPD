package com.ricardo.controlasistenciaipd;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    String codEvento = "";
    String nomEvento = "";
    String recuperado = "";
    Spinner spEventos;
    ArrayList<String> nombresEventos = new ArrayList<String>();
    ArrayList<String> idEventos = new ArrayList<String>();
    String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";
    //String hostIpdProduccion = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    //String hostlocal = "http://10.10.118.16//WebServiceAndroid/";
    //String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        spEventos = (Spinner)findViewById(R.id.sp_menu_eventos);
//        programas.add("Verano 2017");
//        programas.add("Otoño 2017");
//        programas.add("Invierno 2017");
//        programas.add("Primavera 2017");
        //recuperando codigo
        final Bundle recupera=getIntent().getExtras();
        if(recupera!=null){
            recuperado=recupera.getString("cod");
        }
        //cargarSpiner(programas);

        Thread trEventos=new Thread(){
            @Override
            public void run() {
                final String resultado = traerEventos();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cargarSpiner(ArregloSpiner(resultado));
                    }
                });
            }
        };
        trEventos.start();

        spEventos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = position;
                codEvento = idEventos.get(pos);
                nomEvento = nombresEventos.get(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public String traerEventos(){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "ListarEventos.php");
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

    public ArrayList<String> ArregloSpiner(String response){
        ArrayList<String> listado = new ArrayList<String>();
        ArrayList<String> ids = new ArrayList<String>();
        try{
            JSONArray json=new JSONArray(response);
            String texto="";
            for(int i=0; i<json.length();i++){
                codEvento = json.getJSONObject(i).getString("id_evento");
                ids.add(codEvento);
                nomEvento=json.getJSONObject(i).getString("descripcion");
                listado.add(nomEvento);
            }
            nombresEventos = listado;
            idEventos = ids;
        }catch(Exception e){}
        return listado;
    }

    public void cargarSpiner(ArrayList<String> datos){
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.custom_spinner,datos);
        spEventos.setAdapter(adaptador);
    }

    public void goAsistencia(View view){
        Intent i = new Intent(getApplicationContext(), AsistenciaActivity.class);
        i.putExtra("cod", recuperado);
        i.putExtra("eve", codEvento);
        i.putExtra("nomEve",nomEvento);
        startActivity(i);
    }
    public void goReporte(View view){
        Intent i = new Intent(getApplicationContext(), ReportesActivity.class);
        i.putExtra("cod", recuperado);
        startActivity(i);
    }
    @SuppressLint("NewApi")
    public void salirApp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro que desea salir de la aplicación?")
                .setTitle("Aviso")
                .setCancelable(false)
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finishAffinity();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @SuppressLint("NewApi")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Está seguro que desea salir de la aplicación?")
                    .setTitle("Aviso")
                    .setCancelable(false)
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton("Sí",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finishAffinity();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
