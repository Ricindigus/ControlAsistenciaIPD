package com.ricardo.controlasistenciaipd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //String hostIpdDesarrollo = "http://181.65.214.123:8082/sisweb/controlasistencia/";
    //String hostIpdProduccion = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    //String hostlocal = "http://10.10.118.16//WebServiceAndroid/";
    String hostIpdDesarrollo = "http://appweb.ipd.gob.pe/sisweb/controlasistencia/";
    Button btnIngresar;
    EditText txtDni,txtPas;
    int codPonente = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtDni=(EditText)findViewById(R.id.txtDNI);
        txtPas=(EditText)findViewById(R.id.txtPass);
        btnIngresar=(Button)findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(this);

    }

    public String enviarDatosGET(String usu, String pas){
        URL url=null;
        String linea="";
        int respuesta=0;
        StringBuilder resul=null;
        try{
            url=new URL(hostIpdDesarrollo+"logueo.php?dni="+usu+"&pas="+pas);
            HttpURLConnection conection=(HttpURLConnection)url.openConnection();
            respuesta=conection.getResponseCode();
            resul=new StringBuilder();
            if(respuesta==HttpURLConnection.HTTP_OK){
                InputStream in=new BufferedInputStream(conection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                while((linea=reader.readLine())!=null){
                    resul.append(linea);
                }
            }
        }catch (Exception e){}
        return resul.toString();
    }

    public int obtDatosJSON(String response){
        int estadoFinal = 0;
        try{
            JSONArray json=new JSONArray(response);
            for(int i=0;i<json.length();i++){
                estadoFinal = Integer.parseInt(json.getJSONObject(i).getString("estado_final"));
                codPonente = Integer.parseInt(json.getJSONObject(i).getString("id_ponente_final"));
            }
        }catch(Exception e){}
        return estadoFinal;
    }

    @Override
    public void onClick(View v) {
        Thread tr=new Thread(){
            @Override
            public void run() {
                final String resultado=enviarDatosGET(txtDni.getText().toString(), txtPas.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int r = obtDatosJSON(resultado);
                        if (r == 1) {
                            Intent i = new Intent(getApplicationContext(), AsistenciaActivity.class);
                            i.putExtra("cod", codPonente+"");
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Usuario o Password Incorrectos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }
}
