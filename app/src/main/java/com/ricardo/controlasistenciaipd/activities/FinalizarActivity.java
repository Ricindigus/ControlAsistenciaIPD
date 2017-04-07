package com.ricardo.controlasistenciaipd.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ricardo.controlasistenciaipd.R;

public class FinalizarActivity extends AppCompatActivity {

    int codigoError = 0;
    String codEvento = "",codPonente = "", nomEvento = "";
    TextView txtResultado;
    TextView txtMensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar);

        txtResultado = (TextView)findViewById(R.id.txtResultado);
        txtMensaje = (TextView)findViewById(R.id.txtMensaje);

        Bundle recupera = getIntent().getExtras();
        if(recupera != null){
            codigoError = recupera.getInt("codigoError");
            codPonente = recupera.getString("codigoPonente");
            codEvento = recupera.getString("codigoEvento");
            nomEvento = recupera.getString("nombreEvento");
        }

        if(codigoError == 1){
            txtResultado.setText("Error!");
            txtMensaje.setText("Fuera de Día");
        }

    }

    public void goSalirAlMenu(View view){
        finish();
        GuardarActivity.actividad.finish();
        ConfirmarActivity.actividad.finish();
        AsistenciaActivity.actividad.finish();
    }

    public void goContinuarGuardando(View view){
        GuardarActivity.actividad.finish();
        ConfirmarActivity.actividad.finish();
        AsistenciaActivity.actividad.finish();
        Intent i = new Intent(this, AsistenciaActivity.class);
        i.putExtra("codigoPonente", codPonente);
        i.putExtra("codigoEvento", codEvento);
        i.putExtra("nombreEvento",nomEvento);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
    }

}
