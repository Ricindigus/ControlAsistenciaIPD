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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsistenciaActivity extends AppCompatActivity {

    String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";
    //String hostIpdProduccion = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    //String hostlocal = "http://10.10.118.16//WebServiceAndroid/";
    //String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";
    RecyclerView recycler;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager lManager;
    TextView txtEvento, txtDocente, txtDisciplinaFecha;
    Spinner spHorarios, spComplejos;

    String recuperadoCodDocente="";
    String recuperadoCodEvento = "";
    String codHorario="";
    String codComplejo ="";
    String fechaHoy="";
    ArrayList<String> idComplejos = new ArrayList<String>();
    ArrayList<String> idHorarios = new ArrayList<String>();
    ArrayList<Alumno> items = new ArrayList<Alumno>();

    ArrayList<String> horarios = new ArrayList<String>();
    ArrayList<String> complejos = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);
//        horarios.add("LUN/MIE/VIE 14:00-15:00");
//        horarios.add("LUN/MIE/VIE 15:00-16:00");
//        horarios.add("LUN/MIE/VIE 16:00-17:00");
//        horarios.add("MAR/JUE 14:00-15:00");
//        horarios.add("MAR/JUE 15:00-16:00");
//        horarios.add("MAR/JUE 16:00-17:00");
//        horarios.add("SAB 08:00-09:00");
//        horarios.add("SAB 09:00-10:00");
//        horarios.add("SAB 10:00-11:00");
//        complejos.add("ESTADIO NACIONAL");
//        complejos.add("ESTADIO DELLE ALPHI");
//        complejos.add("ESTADIO CAMP NOU");
//        complejos.add("EMIRATES STADIUM");
//        complejos.add("ESTADIO SANTIAGO BERNABEU");
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));
//        items.add(new Alumno("","Denis Ricardo","Morales Retamozo",false));
//        items.add(new Alumno("","Alan Arnold","Ramos Perales",false));

        //recuperando codigo
        final Bundle recupera=getIntent().getExtras();
        if(recupera!=null){
            recuperadoCodDocente=recupera.getString("cod");
            recuperadoCodEvento = recupera.getString("eve");
        }

        txtEvento = (TextView)findViewById(R.id.txt_asistencia_evento);
        txtDocente = (TextView)findViewById(R.id.txt_asistencia_docente);
        txtDisciplinaFecha = (TextView)findViewById(R.id.txt_asistencia_disciplina_fecha);
        spComplejos = (Spinner)findViewById(R.id.sp_asistencia_complejos);
        spHorarios=(Spinner)findViewById(R.id.sp_asistencia_horarios);
//        cargarSpiner(complejos,0);
//        cargarSpiner(horarios,1);
//        cargarRecyclerView(items);

        Thread tr2=new Thread(){
            @Override
            public void run() {
//                final String resultado=traerDetalles(recuperado);
                final String resultado1=traerComplejos(recuperadoCodEvento, recuperadoCodDocente);
                final String resultado2=traerHorarios(recuperadoCodEvento, codComplejo,recuperadoCodDocente);
                try{
                    JSONArray json=new JSONArray(resultado2);
                    codHorario = json.getJSONObject(0).getString("id_disciplinaevento");
//                    json=new JSONArray(resultado);
//                    fechaHoy = json.getJSONObject(0).getString("hoy");
                }catch(Exception e){}
                final String resultado3=traerAlumnos(recuperadoCodEvento, codHorario);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                            mostrarDetalles(resultado);
                        cargarSpiner(ArregloSpiner(resultado1,0),0);
                        cargarSpiner(ArregloSpiner(resultado2,1),1);
                        cargarRecyclerView(ArregloLista(resultado3));
                    }
                });
            }
        };
        tr2.start();
//
        spHorarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final String codigoHorario = idHorarios.get(pos);
                codHorario = codigoHorario;
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        final String resultado4 = traerAlumnos(recuperadoCodEvento, codigoHorario);
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

        spComplejos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final String codigoComplejo = idComplejos.get(pos);
                codComplejo = codigoComplejo;
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        final String resultado4 = traerHorarios(recuperadoCodEvento, codigoComplejo,recuperadoCodDocente);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cargarSpiner(ArregloSpiner(resultado4,1),1);
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
    public String traerAlumnos(String codEve, String codHor){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url = new URL(hostIpdDesarrollo + "ListarAlumnos.php?ide=" + codEve + "&hor="+ codHor);
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
            JSONArray json=new JSONArray(response);
            for(int i=0;i<json.length();i++){
                /*txtPuntoDesarrollo.setText(json.getJSONObject(i).getString("complejo"));
                txtDisciplina.setText(json.getJSONObject(i).getString("disciplina"));*/
            }
        }catch(Exception e){}
    }

    public ArrayList<String> ArregloSpiner(String response, int spinner){
        ArrayList<String> listado=new ArrayList<String>();
        if(spinner == 1){
            try{
                JSONArray json=new JSONArray(response);
                String texto="";
                for(int i=0; i<json.length();i++){
                    codHorario = json.getJSONObject(i).getString("id_disciplinaevento");
                    idHorarios.add(codHorario);
                    texto=json.getJSONObject(i).getString("horario");
                    listado.add(texto);
                }
            }catch(Exception e){}
        }else{
            try{
                JSONArray json=new JSONArray(response);
                String texto="";
                for(int i=0; i<json.length();i++){
                    codComplejo = json.getJSONObject(i).getString("id_complejo");
                    idComplejos.add(codComplejo);
                    texto=json.getJSONObject(i).getString("complejo");
                    listado.add(texto);
                }
            }catch(Exception e){}
        }
        return listado;
    }
    //METODO QUE PERMITE CARGAR EL SPINNER

    public void cargarSpiner(ArrayList<String> datos, int spinner){
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.custom_spinner,datos);
        if(spinner == 1) spHorarios.setAdapter(adaptador);
        else spComplejos.setAdapter(adaptador);
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

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);

        // Crear un nuevo adaptador
        adapter = new AlumnoAdapter(datos);

        // Obtener el Recycler
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
        intent.putExtra("alumnos", items);
        intent.putExtra("cod",recuperadoCodDocente);
        intent.putExtra("evento", recuperadoCodEvento);
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
                                    i.putExtra("cod", recuperadoCodDocente);
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
                                i.putExtra("cod", recuperadoCodDocente);
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
