package com.ricardo.controlasistenciaipd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
    private String codHorario="";
    private String nomEvento = "";
    private String nomComplejo = "";
    private String nomDocente = "";
    private String nomDisciplina = "";
    private String nomHorario = "";
    private String fechaHoy ="";
    private ArrayList<Alumno> asistenciaAlumnos;
    public static Activity actividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);
        asistenciaAlumnos = new ArrayList<Alumno>();
        actividad = this;
        Bundle recupera = getIntent().getExtras();
        if(recupera != null){
            codigoPonente = recupera.getString("codigoPonente");
            codEvento = recupera.getString("codigoEvento");
            nomEvento = recupera.getString("nombreEvento");
            nomComplejo = recupera.getString("nombreComplejo");
            nomDocente = recupera.getString("nombreDocente");
            nomDisciplina = recupera.getString("nombreDisciplina");
            nomHorario = recupera.getString("nombreHorario");
            codHorario = recupera.getString("codigoHorario");
            fechaHoy = recupera.getString("fecha");
            asistenciaAlumnos = (ArrayList<Alumno>)recupera.getSerializable("alumnos");
        }
//        showToolbar("Confirmar Asistencia",true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_asistencia);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Confirmar Asistencia");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
        intent.putExtra("codigoPonente",codigoPonente);
        intent.putExtra("codigoEvento",codEvento);
        intent.putExtra("codigoHorario",codHorario);
        intent.putExtra("fecha",fechaHoy);
        intent.putExtra("nombreEvento",nomEvento);
        startActivity(intent);
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
//    @SuppressLint("NewApi")
//    public void salirApp(View view){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("¿Está seguro que desea salir? (Se perderán los datos no guardados)")
//                .setTitle("Aviso")
//                .setCancelable(false)
//                .setNegativeButton("No",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        })
//                .setPositiveButton("Sí",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Intent i = new Intent(getApplicationContext(), MenuActivity.class);
//                                i.putExtra("cod", codigoPonente);
//                                startActivity(i);
//                            }
//                        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }
//    public void showToolbar(String title, boolean upButton){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_asistencia);
//        toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(title);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
//    }

}
