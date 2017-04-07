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

import java.util.ArrayList;

public class DetalleAlumnoActivity extends AppCompatActivity {
    String nombres = "";
    String apellidos = "";
    String codigoPonente = "";
    boolean sexo = true;
    String dni = "";
    TextView txtNombres, txtApellidos, txtDni;
    ImageView imgAlumno1, imgAlumno2;
    RecyclerView recyclerDisciplina1, recyclerDisciplina2;
    LinearLayoutManager lManager1, lManager2;
    DisciplinaAdapter adapter1, adapter2;
    ArrayList<Asistencia> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alumno);
        Bundle recupera = getIntent().getExtras();
        if(recupera != null){
            codigoPonente = recupera.getString("codigoPonente");
            nombres = recupera.getString("nombres");
            apellidos = recupera.getString("apellidos");
            dni = recupera.getString("dni");
            sexo = recupera.getBoolean("sexo");
        }
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

        items = new ArrayList<Asistencia>();
        items.add(new Asistencia("01/07/2017","A"));
        items.add(new Asistencia("08/07/2017","A"));
        items.add(new Asistencia("15/07/2017","A"));
        items.add(new Asistencia("22/07/2017","F"));
        items.add(new Asistencia("29/07/2017","A"));
        items.add(new Asistencia("01/08/2017","A"));
        items.add(new Asistencia("08/08/2017","A"));
        items.add(new Asistencia("15/082017","A"));
        items.add(new Asistencia("22/08/2017","A"));
        items.add(new Asistencia("29/08/2017","A"));
        items.add(new Asistencia("01/09/2017","A"));
        items.add(new Asistencia("08/09/2017","A"));
        items.add(new Asistencia("15/09/2017","A"));
        items.add(new Asistencia("22/09/2017","A"));
        items.add(new Asistencia("29/09/2017","A"));
        items.add(new Asistencia("01/10/2017","A"));
        items.add(new Asistencia("08/10/2017","F"));
        items.add(new Asistencia("15/10/2017","A"));
        items.add(new Asistencia("22/10/2017","A"));
        items.add(new Asistencia("29/10/2017","A"));



        txtNombres = (TextView)findViewById(R.id.txt_reporte_alumno_nombres);
        txtApellidos = (TextView)findViewById(R.id.txt_reporte_alumno_apellidos);
        txtDni = (TextView)findViewById(R.id.txt_reporte_alumno_dni);
        imgAlumno1 = (ImageView)findViewById(R.id.img_reporte_alumno_boy);
        imgAlumno2 = (ImageView)findViewById(R.id.img_reporte_alumno_girl);
        recyclerDisciplina1 = (RecyclerView)findViewById(R.id.recycler_disciplina_1);
        recyclerDisciplina2 = (RecyclerView)findViewById(R.id.recycler_disciplina_2);

        txtNombres.setText(nombres);
        txtApellidos.setText(apellidos);
        txtDni.setText(dni);

        if(sexo == false){
            imgAlumno2.setVisibility(View.VISIBLE);
        }


        recyclerDisciplina1.setHasFixedSize(true);
        recyclerDisciplina2.setHasFixedSize(true);

        lManager1 = new LinearLayoutManager(this);
        recyclerDisciplina1.setLayoutManager(lManager1);
        lManager2 = new LinearLayoutManager(this);
        recyclerDisciplina2.setLayoutManager(lManager2);

        adapter1= new DisciplinaAdapter(items);
        recyclerDisciplina1.setAdapter(adapter1);
        adapter2 = new DisciplinaAdapter(items);
        recyclerDisciplina2.setAdapter(adapter2);
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
}
