package com.example.apiofthronesproject.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.apiofthronesproject.R;
import com.example.apiofthronesproject.adapter.RecyclerAdapter;
import com.example.apiofthronesproject.io.HttpConnectAOT;
import com.example.apiofthronesproject.model.Personaje;

import org.json.JSONArray;
import org.json.JSONException;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    private static boolean notificaciones;
    RecyclerView recyclerPersonajes;
    RecyclerAdapter adapter;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    AlertDialog.Builder adBuilder;
    ItemTouchHelper.SimpleCallback simpleCallback;
    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Uso esto para comprobar si se está (o no) conectado a la red
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        //Compruebo las preferencias preestablecidas
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        notificaciones = preferences.getBoolean("notificaciones", true);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        adapter = new RecyclerAdapter(this);

        new taskConnections().execute("GET", "/Characters");

        recyclerPersonajes = (RecyclerView) findViewById(R.id.recView);
        recyclerPersonajes.setLayoutManager(new LinearLayoutManager(this));
        recyclerPersonajes.setAdapter(adapter);

        //Creo el listenner para la lista
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createToasty("info",MainActivity.this, "Info: "+adapter.listPersonajes.get(recyclerPersonajes.getChildAdapterPosition(view)).getNombre());

                Intent i = new Intent(MainActivity.this, CharacterActivity.class);
                i.putExtra("id", adapter.listPersonajes.get(recyclerPersonajes.getChildAdapterPosition(view)).getId());
                startActivity(i);
                view.setSelected(true);
            }
        });

        //Compruebo el estado de la conexión
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            //Aqui toodo esta bien ya que estamos conectados a internet
        }else{
            createAlertDialog("Comrpueba la conexión a internet", "¡Sin conexión!");
        }

        //Uso esto para eliminar un elemento del recyclerView al deslizarlo a la derecha
        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.listPersonajes.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerPersonajes);
    }

    //Menu flotante
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.floating_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = -1;

        id = item.getItemId();

        switch (id){
            case R.id.mnuAjustes:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.mnuSalir:
                finish();
                System.exit(0);
                break;
            case R.id.mnuSesion:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("inicioSesion", false);
                editor.commit();
                finish();
                System.exit(0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //Obtener datos de la API y trabajar con ellos
    private class taskConnections extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            switch (strings[0]){
                case "GET":
                    result = HttpConnectAOT.getRequest(strings[1]);
                    break;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (s != null) {
                    JSONArray jsonArray = new JSONArray(s);
                    String id = "";
                    String nombre = "";
                    String titulo = "";
                    String familia = "";
                    String imgUrl = "";
                    Personaje aux = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        id = jsonArray.getJSONObject(i).get("id").toString();
                        nombre = jsonArray.getJSONObject(i).getString("fullName");
                        titulo = jsonArray.getJSONObject(i).getString("title");
                        familia = jsonArray.getJSONObject(i).getString("family");
                        imgUrl = jsonArray.getJSONObject(i).getString("imageUrl");

                        aux = new Personaje(id,nombre,titulo,familia,imgUrl);
                        Log.d("D", "Personaje: "+ aux.toString());
                        adapter.addPersonaje(aux);
                    }
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Creacion de toast
    public void makeToast(String text) {
        if(notificaciones == true){
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    //Cargar las preferencias preselecciondas
    public void loadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean notif = sharedPreferences.getBoolean("notificaciones",true);
        notificaciones = notif;
    }

    //Creacion de los alert dialogs
    public void createAlertDialog(String msg, String titulo) {
        adBuilder = new AlertDialog.Builder(this);
        adBuilder.setMessage(msg).setTitle(titulo);
        adBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        if(notificaciones == true){
            adBuilder.show();
        }
    }

    public void createToasty(String key, Context c, String msg){
        if(notificaciones == true){
            switch (key){
                case "error":
                    Toasty.error(c, msg).show();
                    break;
                case "info":
                    Toasty.info(c, msg).show();
                    break;
                case "success":
                    Toasty.success(c, msg).show();
                    break;
                case "warning":
                    Toasty.warning(c, msg).show();
                    break;
                case "normal":
                    Toasty.normal(c, msg).show();
                    break;
                default:
                    Toasty.normal(c, msg).show();
                    break;
            }
        }
    }

}