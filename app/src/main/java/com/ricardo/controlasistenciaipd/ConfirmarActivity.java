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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ConfirmarActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private ArrayList<Alumno> asistenciaAlumnos;
    String codigoPonente="";
    String codEvento="";
    String fechaHoy ="";
    TextView txtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);

        asistenciaAlumnos = new ArrayList<Alumno>();

        txtFecha = (TextView)findViewById(R.id.txtFecha);

        Bundle recupera = getIntent().getExtras();

        if(recupera != null){
            asistenciaAlumnos = (ArrayList<Alumno>)recupera.getSerializable("alumnos");
            codigoPonente = recupera.getString("cod");
            codEvento = recupera.getString("evento");
            fechaHoy = recupera.getString("fecha");
        }

        txtFecha.setText(fechaHoy);

        recycler = (RecyclerView) findViewById(R.id.recyclerConfirmar);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        adapter = new AlumnoConfirmarAdapter(asistenciaAlumnos);
        recycler.setAdapter(adapter);
    }

    public void goGuardar(View view){
        Intent intent = new Intent(this, GuardarActivity.class);
        intent.putExtra("alumnos", asistenciaAlumnos);
        intent.putExtra("cod",codigoPonente);
        intent.putExtra("evento",codEvento);
        intent.putExtra("fecha",fechaHoy);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    public void salirApp(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro que desea salir? (Ningún dato o cambio se guardará)")
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

}
