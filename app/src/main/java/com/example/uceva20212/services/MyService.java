package com.example.uceva20212.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MostrarFecha motrarHora = new MostrarFecha();
        motrarHora.execute();
    }
    public class MostrarFecha extends AsyncTask<Void,String,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 20; i++) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                publishProgress(formatter.format(date));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("","Date: "+ formatter.format(date));
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(),values[0], Toast.LENGTH_LONG).show();
        }
    }
}