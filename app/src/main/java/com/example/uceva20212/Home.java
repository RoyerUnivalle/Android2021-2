package com.example.uceva20212;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.uceva20212.services.MyService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends AppCompatActivity implements View.OnClickListener{

    Button btnDelegado, btninterface, btnContador, btnPintar;
    String password, username;
    TextView tv1;
    int contador = 0;
    EditText etContador;
    Pintar obj; // objeto de la clase pintar
    DataHttpClass HttpObj;
    Intent servicio;
    int i;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
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
        // getDataVolley();
        // getJsonVolley();
        /*try {
            getDataHttp(); // Error porque no se pone en un hilo independiente
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        NetworkInfo nInfo = cm.getNetworkInfo(cm.getActiveNetwork());
        if(nInfo.getType() == ConnectivityManager.TYPE_WIFI){
            HttpObj = new DataHttpClass();
            HttpObj.execute();
        }else if (nInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            Toast.makeText(this, "Desea continuar con datos", Toast.LENGTH_LONG).show();
        }
        servicio = new Intent(this, MyService.class);
        /////////////// Fragmentos a nivel programatico
        /*fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        BlankFragment2 fragmento1 = new BlankFragment2();
        fragmentTransaction.add(R.id.reuseFragment, fragmento1);
        fragmentTransaction.commit();*/
        /////////////// Fragmentos a nivel programatico
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
public void getDataVolley(){
// Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this);
    String url ="https://www.google.com";
    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    Log.d("","Codigo Google: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("","Error: "+ error);
        }
    });
    // Add the request to the RequestQueue.
    queue.add(stringRequest);
}

    public void getJsonVolley(){
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://run.mocky.io/v3/d0dc703a-1a0c-49b1-9146-f7ba5b92088c";
        // Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        //Log.d("","Codigo Google: "+ response.substring(0,500));
                        try {
                            JSONArray estudiantes = response.getJSONArray("estudiantes");
                            int cantidadEstudiantes = estudiantes.length();
                            Log.d("","cantidadEstudiantes: "+ cantidadEstudiantes);
                            for (int i = 0; i < cantidadEstudiantes; i++){
                                JSONObject estudiante = estudiantes.getJSONObject(i);
                                Log.d("","Nombre: "+ estudiante.getString("nombre") + " "+ estudiante.getString("apellido"));
                                JSONArray materias = estudiante.getJSONArray("materias");
                                int cantidadMaterias = materias.length();
                                for (int j=0; j<cantidadMaterias; j++){
                                    JSONObject materia = materias.getJSONObject(j);
                                    Log.d("","Materia: "+ materia.getString("name")+ " " + materia.getString("period") );
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("","Error: "+ error);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }

    public void getDataHttp() throws IOException {
        URL url = new URL("https://run.mocky.io/v3/d0dc703a-1a0c-49b1-9146-f7ba5b92088c");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Log.d("","Codigo Google: "+ in);
        } finally {
            urlConnection.disconnect();
        }
    }

    public class DataHttpClass extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://run.mocky.io/v3/d0dc703a-1a0c-49b1-9146-f7ba5b92088c");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            urlConnection.setRequestProperty("Accept", "application/json");
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Log.d("","Respuesta httpUrlConecction: "+ in);
                // Procesar repuesta para manipular un JSON OBJECT //
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return null;
        }
    }

    public void iniciarServicioFecha(View x){
        //Intent servicio = new Intent(this, MyService.class);
        startService(servicio);
    }

    public void detenerServicioFecha(){
        //Intent servicio = new Intent(this, MyService.class);
        //stopService(servicio);
    }
    // nos ensambla el munu en la activity. ¿Con cuantas opcions?
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.vertical_menu, menu); // <<-- con los items que estan aqui
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemF1:
                //Toast.makeText(this,"itemF1", Toast.LENGTH_LONG).show();
                this.cambiarFragmento("itemF1");
                break;
            case R.id.itemF2:
                this.cambiarFragmento("itemF2");
                break;
            default:
                Toast.makeText(this,"itemF2", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void cambiarFragmento(String fragmentName){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentManager.popBackStack();
        if(fragmentName.matches("itemF1")){
            ItemFragment fragmento1 = new ItemFragment();
            fragmentTransaction.add(R.id.reuseFragment, fragmento1);
            fragmentTransaction.commit();
        }else {
            BlankFragment2 fragmento1 = new BlankFragment2();
            fragmentTransaction.add(R.id.reuseFragment, fragmento1);
            fragmentTransaction.commit();
        }

    }
}