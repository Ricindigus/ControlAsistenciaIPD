package com.ricardo.controlasistenciaipd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetalleAlumnoActivity extends AppCompatActivity {
    String detalle = "";
    TextView txtDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alumno);
        Bundle recupera = getIntent().getExtras();
        if(recupera != null){
            detalle = recupera.getString("detalle");
        }
        txtDetalle = (TextView)findViewById(R.id.txt_detalle);
        txtDetalle.setText(detalle);
    }
}
