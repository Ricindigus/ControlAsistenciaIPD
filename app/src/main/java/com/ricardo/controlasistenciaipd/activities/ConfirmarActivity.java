package com.ricardo.controlasistenciaipd.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.R;
import com.ricardo.controlasistenciaipd.adapters.AlumnoConfirmarAdapter;
import com.ricardo.controlasistenciaipd.pojos.Alumno;

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

    private String codPonente="";
    private String codEvento="";
    private String codHorario="";
    private String nomEvento = "";
    private String nomComplejo = "";
    private String nomPonente = "";
    private String nomDisciplina = "";
    private String nomHorario = "";
    private String fechaHoy ="";
    private ArrayList<Alumno> asistenciaAlumnos;

    private Toolbar toolbar;
    public static Activity actividad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);
        asistenciaAlumnos = new ArrayList<Alumno>();
        actividad = this;
        Bundle recupera = getIntent().getExtras();
        if(recupera != null){
            codPonente = recupera.getString("codigoPonente");
            codEvento = recupera.getString("codigoEvento");
            nomEvento = recupera.getString("nombreEvento");
            nomComplejo = recupera.getString("nombreComplejo");
            nomPonente = recupera.getString("nombreDocente");
            nomDisciplina = recupera.getString("nombreDisciplina");
            nomHorario = recupera.getString("nombreHorario");
            codHorario = recupera.getString("codigoHorario");
            fechaHoy = recupera.getString("fecha");
            asistenciaAlumnos = (ArrayList<Alumno>)recupera.getSerializable("alumnos");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_asistencia);
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
        txtDocente.setText(nomPonente);
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
        intent.putExtra("codigoPonente",codPonente);
        intent.putExtra("codigoEvento",codEvento);
        intent.putExtra("codigoHorario",codHorario);
        intent.putExtra("fecha",fechaHoy);
        intent.putExtra("nombreEvento",nomEvento);
        startActivity(intent);
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
            i.putExtra("codigoPonente", codPonente);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
