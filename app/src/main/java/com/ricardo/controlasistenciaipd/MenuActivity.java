package com.ricardo.controlasistenciaipd;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    String recuperado = "";
    Spinner spProgramas;
    ArrayList<String> programas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        spProgramas = (Spinner)findViewById(R.id.spProgramas);
        programas = new ArrayList<String>();
        programas.add("Verano 2017");
        programas.add("Otoño 2017");
        programas.add("Invierno 2017");
        programas.add("Primavera 2017");
        //recuperando codigo
        final Bundle recupera=getIntent().getExtras();
        if(recupera!=null){
            recuperado=recupera.getString("cod");
        }
        cargarSpiner(programas);
    }

    public void cargarSpiner(ArrayList<String> datos){
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.custom_spinner,datos);
        spProgramas.setAdapter(adaptador);
    }

    public void goAsistencia(View view){
        Intent i = new Intent(getApplicationContext(), AsistenciaActivity.class);
        i.putExtra("cod", recuperado);
        startActivity(i);
    }
    public void goReporte(View view){
        Intent i = new Intent(getApplicationContext(), Main2Activity.class);
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
