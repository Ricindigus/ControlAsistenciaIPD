package com.ricardo.controlasistenciaipd;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class ReporteGeneralActivity extends AppCompatActivity {
    String recuperado="";
    int dia, mes, anio;
    ImageButton btnCalendar1;
    ImageButton btnCalendar2;
    TextView txtFecha1;
    TextView txtFecha2;
    Spinner spHorarios;
    ArrayList<String> horarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_general);
        horarios = new ArrayList<String>();
        horarios.add("L/X/V 14:00-15:00");
        horarios.add("L/X/V 15:00-16:00");
        horarios.add("L/X/V 16:00-17:00");
        horarios.add("M/J 14:00-15:00");
        horarios.add("M/J 15:00-16:00");
        horarios.add("M/J 16:00-17:00");
        horarios.add("S 08:00-09:00");
        horarios.add("S 09:00-10:00");
        horarios.add("S 10:00-11:00");

        spHorarios = (Spinner)findViewById(R.id.spHorarios);
        cargarSpiner(horarios);
        //recuperando codigo
        final Bundle recupera=getIntent().getExtras();
        if(recupera!=null){
            recuperado=recupera.getString("cod");
        }
        btnCalendar1 = (ImageButton)findViewById(R.id.btn_calendario1);
        btnCalendar2 = (ImageButton)findViewById(R.id.btn_calendario2);
        txtFecha1 = (TextView)findViewById(R.id.txtFecha1);
        txtFecha2 = (TextView)findViewById(R.id.txtFecha2);

        Calendar calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        anio = calendar.get(Calendar.YEAR);
        String mAux = "" + mes;
        if(mes < 10) mAux = "0" + mAux;
        String dAux = "" + dia;
        if(dia < 10) dAux = "0" + dAux;

        txtFecha1.setText(dAux + "/" + mAux + "/" + anio);
        txtFecha2.setText(dAux + "/" + mAux + "/" + anio);
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

    public void setFecha1(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++ ;
                String d = "" + dayOfMonth;
                String m = "" + month;
                if(month < 10) m = "0" + m;
                if(dayOfMonth < 10) d = "0" + d;
                txtFecha1.setText(d + "/" + m + "/" + year);
            }
        },anio,mes,dia);
        datePickerDialog.show();
    }

    public void setFecha2(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++ ;
                String d = "" + dayOfMonth;
                String m = "" + month;
                if(month < 10) m = "0" + m;
                if(dayOfMonth < 10) d = "0" + d;
                txtFecha2.setText(d + "/" + m + "/" + year);
            }
        },anio,mes,dia);
        datePickerDialog.show();
    }
    public void cargarSpiner(ArrayList<String> datos){
        ArrayAdapter<String> adaptador=new ArrayAdapter<String>(this,R.layout.custom_spinner,datos);
        spHorarios.setAdapter(adaptador);
    }

}
