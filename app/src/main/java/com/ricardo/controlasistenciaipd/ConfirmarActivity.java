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
    private TextView txtEvento;
    private TextView txtComplejo;
    private TextView txtDocente;
    private TextView txtDisciplinaFecha;
    private TextView txtHorario;

    private String codigoPonente="";
    private String codEvento="";
    private String nomEvento = "";
    private String nomComplejo = "";
    private String nomDocente = "";
    private String nomDisciplina = "";
    private String nomHorario = "";
    private String fechaHoy ="";
    private ArrayList<Alumno> asistenciaAlumnos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);
        asistenciaAlumnos = new ArrayList<Alumno>();
        Bundle recupera = getIntent().getExtras();
        if(recupera != null){
            codigoPonente = recupera.getString("cod");
            codEvento = recupera.getString("evento");

            nomEvento = recupera.getString("nombreEvento");
            nomComplejo = recupera.getString("nombreComplejo");
            nomDocente = recupera.getString("nombreDocente");
            nomDisciplina = recupera.getString("nombreDisciplina");
            nomHorario = recupera.getString("nombreHorario");
            fechaHoy = recupera.getString("fecha");

            asistenciaAlumnos = (ArrayList<Alumno>)recupera.getSerializable("alumnos");
        }

        txtEvento = (TextView)findViewById(R.id.txt_confirmar_evento);
        txtComplejo = (TextView)findViewById(R.id.txt_confirmar_complejo);
        txtDocente = (TextView)findViewById(R.id.txt_confirmar_docente);
        txtDisciplinaFecha = (TextView)findViewById(R.id.txt_confirmar_disciplina_fecha);
        txtHorario = (TextView)findViewById(R.id.txt_confirmar_horario);

        txtEvento.setText(nomEvento);
        txtComplejo.setText(nomComplejo);
        txtDocente.setText(nomDocente);
        txtDisciplinaFecha.setText(nomDisciplina + " - " + fechaHoy);
        txtHorario.setText(nomHorario);
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
                                i.putExtra("cod", codigoPonente);
                                startActivity(i);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
