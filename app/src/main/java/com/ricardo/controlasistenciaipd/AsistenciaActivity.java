package com.ricardo.controlasistenciaipd;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsistenciaActivity extends AppCompatActivity {

    String hostIpdDesarrollo = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    //String hostIpdProduccion = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    //String hostlocal = "http://10.10.118.16//WebServiceAndroid/";
    //String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";
    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager lManager;
    TextView txtPrograma;
    TextView txtPuntoDesarrollo;
    TextView txtDisciplina;
    TextView txtFecha;
    Spinner spHorarios;

    String recuperado="";
    String codDisciplinaEvento="";
    String fechaHoy="";
    ArrayList<String> idEventos = new ArrayList<String>();
    ArrayList<Alumno> items=new ArrayList<Alumno>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);
        //recuperando codigo
        final Bundle recupera=getIntent().getExtras();
        if(recupera!=null){
            recuperado=recupera.getString("cod");
        }

        txtPrograma=(TextView)findViewById(R.id.txtPrograma);
        txtFecha = (TextView)findViewById(R.id.txtFecha);
        txtPuntoDesarrollo = (TextView)findViewById(R.id.txtPuntoDesarrollo);
        txtDisciplina = (TextView)findViewById(R.id.txtDisciplina);
        spHorarios=(Spinner)findViewById(R.id.spHorario);

        Thread tr2=new Thread(){
            @Override
            public void run() {
                final View viewRaiz = findViewById(R.id.rootView);
                final String resultado=traerDetalles(recuperado);
                final String resultado2=traerHorarios(recuperado);
                try{
                    JSONArray json=new JSONArray(resultado2);
                    codDisciplinaEvento = json.getJSONObject(0).getString("id_disciplinaevento");
                    json=new JSONArray(resultado);
                    fechaHoy = json.getJSONObject(0).getString("hoy");
                }catch(Exception e){}
                final String resultado3=traerAlumnos(codDisciplinaEvento);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        if(!resultado2.equals("[]")){
                            mostrarDetalles(resultado);
                            cargarSpiner(ArregloSpiner(resultado2));
                            cargarRecyclerView(ArregloLista(resultado3));
//                        }else{
//                             salirSinDatos(viewRaiz);
//                        }
                    }
                });
            }
        };
        tr2.start();

        spHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final String codigoEvento = idEventos.get(pos);
                codDisciplinaEvento = codigoEvento;
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        final String resultado4 = traerAlumnos(codigoEvento);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cargarRecyclerView(ArregloLista(resultado4));
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

    public String traerDetalles(String codigo){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "listaDetalle.php?cod="+codigo);
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

    public String traerHorarios(String codigo){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "listarhorarios.php?cod="+codigo);
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
    public String traerAlumnos(String codigo){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "listaralumnos.php?horario="+ codigo);
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

    //METODO QUE PERMITE MOSTRAR EL NOMBRE DEL ALUMNO LOGEADO
    public void mostrarDetalles(String response){
        try{
            JSONArray json=new JSONArray(response);
            for(int i=0;i<json.length();i++){
                txtPrograma.setText("Verano 2017");
                txtFecha.setText(fechaHoy);
                txtPuntoDesarrollo.setText(json.getJSONObject(i).getString("complejo"));
                txtDisciplina.setText(json.getJSONObject(i).getString("disciplina"));
            }
        }catch(Exception e){}
    }

    public ArrayList<String> ArregloSpiner(String response){
        ArrayList<String> listado=new ArrayList<String>();
        try{
            JSONArray json=new JSONArray(response);
            String texto="";
            for(int i=0; i<json.length();i++){
                codDisciplinaEvento = json.getJSONObject(i).getString("id_disciplinaevento");
                idEventos.add(codDisciplinaEvento);
                texto=json.getJSONObject(i).getString("horario");
                listado.add(texto);
            }
        }catch(Exception e){}
        return listado;
    }
    //METODO QUE PERMITE CARGAR EL SPINNER
    public void cargarSpiner(ArrayList<String> datos){
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.custom_spinner,datos);
        spHorarios.setAdapter(adaptador);
    }
    public ArrayList<Alumno> ArregloLista(String response){
        try{
            JSONArray json=new JSONArray(response);
            String alumno = null;
            items = new ArrayList<Alumno>();
            for(int i=0; i<json.length();i++){
                items.add(new Alumno(json.getJSONObject(i).getString("id_participante_evento"), json.getJSONObject(i).getString("nombres"), json.getJSONObject(i).getString("ape_pat")
                        + " " + json.getJSONObject(i).getString("ape_mat"),false));
            }
        }catch(Exception e){}
        return items;
    }
    public void cargarRecyclerView(ArrayList<Alumno> datos){
        // Obtener el Recycler
        recycler = (RecyclerView) findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);
        // Crear un nuevo adaptador
        adapter = new AlumnoAdapter(datos);
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
        intent.putExtra("alumnos", items);
        intent.putExtra("cod",recuperado);
        intent.putExtra("evento", codDisciplinaEvento);
        intent.putExtra("fecha",fechaHoy);
        startActivity(intent);
    }


    @SuppressLint("NewApi")
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Está seguro que desea salir? (Se perderán los datos no guardados)")
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
                                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                                    i.putExtra("cod", recuperado);
                                    startActivity(i);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("NewApi")
    public void salirApp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro que desea salir? (Se perderán los datos no guardados)")
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
                                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                                i.putExtra("cod", recuperado);
                                startActivity(i);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("NewApi")
    public void salirSinDatos(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No existen datos para mostrar, ya ha guardado todas las asistencias del día o esta fuera del horario")
                .setTitle("Aviso")
                .setCancelable(false)
                .setPositiveButton("Salir",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finishAffinity();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
