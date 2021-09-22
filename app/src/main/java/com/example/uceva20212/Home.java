package com.example.uceva20212;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements View.OnClickListener{

    Button btnDelegado, btninterface, btnContador, btnPintar;
    String password, username;
    TextView tv1;
    int contador = 0;
    EditText etContador;
    Pintar obj; // objeto de la clase pintar
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Revisión de parametros
        Bundle recibo = getIntent().getExtras();
        password = recibo.getString("password");
        username = recibo.getString("username");
        // Log.d("","password: "+ password);
        // Log.d("","username: "+ username);
        tv1 = findViewById(R.id.tv1);
        tv1.setText(username + " password: " + password);
        // Revisión de parametros
        // enlazamiento
        btnDelegado = findViewById(R.id.btnDelegado);
        btninterface = findViewById(R.id.btnInterface);
        btnContador = findViewById(R.id.botonContar);
        btnPintar = findViewById(R.id.botonPintar);
        etContador = findViewById(R.id.et1);
        // Agregacion de eventos de forma delegada y por interfaz
        btninterface.setOnClickListener(this);
        btnContador.setOnClickListener(this);
        btnDelegado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Hola boton delegado", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void atras(View h){
        Intent ir = new Intent(this, MainActivity.class);
        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(ir);
    }

    public void contar(){
        contador = contador + 1;
        etContador.setText("Conteo: " + contador);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInterface:
                Toast.makeText(this,"Hola boton inferface", Toast.LENGTH_LONG).show();
                break;
            case R.id.botonContar:
                contar();
                break;
            default:
                Toast.makeText(this,"HOLA DEFAULT", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("contador", contador); //key, value
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contador = savedInstanceState.getInt("contador"); // key
    }
    public void pintar(View g){
        //Nativa
        /* for (int i = 0; i < contador; i++) {
            btnPintar.setBackgroundColor(new Color().rgb(aleatoiro(),aleatoiro(),aleatoiro()));
            Thread.sleep(1000);
        }*/

        // Asyntask
        /*if(obj == null){
            obj = new Pintar();
            obj.execute();
        }else {
            obj.cancel(true); // llamar al metodo onCancelled
        }*/

        // Runnable
        if(contador > 0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (i = 0; i < contador; i++) {
                        try {
                            Thread.sleep(1000);
                            btnPintar.setBackgroundColor(Color.rgb(aleatorio(),aleatorio(),aleatorio()));
                            btnPintar.setText("I: "+ i);
                            Home.this.runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    //Do your UI operations like dialog opening or Toast here
                                    Toast.makeText(getApplicationContext(),"CONTADOR "+i, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                public int aleatorio(){
                    return (int) (Math.random()* 255) + 1;
                }
            }).start();
        }
        else {
            Log.d("","CONTADOR EN 0");
            Toast.makeText(getApplicationContext(),"CONTADOR EN 0", Toast.LENGTH_LONG).show();
        }
    }
    public int aleatoiro(){
        int colorAleatorio = (int) (Math.random()* 256 + 1);
        Log.d("","colorAleatorio: "+ colorAleatorio);
        return colorAleatorio;
    }
public class  Pintar extends AsyncTask<Void,Integer,Void>{// parametro, progreso, resultado
    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i <= contador; i++) {
            if(isCancelled()){
                break;
            }else{
                btnPintar.setBackgroundColor(new Color().rgb(aleatoiro(),aleatoiro(),aleatoiro()));
                publishProgress(i); //<< - para acceder a la ui
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... enteros) {
        super.onProgressUpdate(enteros);
        //btnPintar.setBackgroundColor(new Color().rgb(aleatoiro(),aleatoiro(),aleatoiro()));
        btnPintar.setText("I: "+ enteros[0]);
        Toast.makeText(getApplicationContext(),"Contador: "+ enteros[0], Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        //obj.cancel(true);
        obj = null;
        Toast.makeText(getApplicationContext(),"Proceso Cancelado", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPreExecute() {
        //btnPintar.setEnabled(false);
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        obj = null;
        //btnPintar.setEnabled(true);
        Toast.makeText(getApplicationContext(),"Proceso finalizado", Toast.LENGTH_LONG).show();
    }
}

}