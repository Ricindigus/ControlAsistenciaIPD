package com.ricardo.controlasistenciaipd;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class FinalizarActivity extends AppCompatActivity {

    int codigoError = 0;
    String codPonente = "";
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
            codigoError = recupera.getInt("error");
            codPonente = recupera.getString("codigoPonente");
        }

        if(codigoError == 1){
            txtResultado.setText("Error!");
            txtMensaje.setText("Fuera de Día");
        }

    }
    @SuppressLint("NewApi")
    public void goSalir(View view){
        finishAffinity();
    }

    public void goContinuarGuardando(View view){
        Intent intent = new Intent(this, AsistenciaActivity.class);
        intent.putExtra("cod",codPonente);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

}
