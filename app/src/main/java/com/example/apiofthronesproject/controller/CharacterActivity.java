package com.example.apiofthronesproject.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apiofthronesproject.R;
import com.example.apiofthronesproject.adapter.RecyclerAdapter;
import com.example.apiofthronesproject.io.HttpConnectAOT;
import com.example.apiofthronesproject.model.Personaje;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
//Esta clase se encarga de la vista detallada de cada personaje
public class CharacterActivity extends AppCompatActivity {
    private ImageView imgPersonajeDetalle;
    private TextView txtNombre;
    private TextView txtTitulo;
    private TextView txtFamilia;
    private String iId;
    private Context c;
    private CircularProgressDrawable cpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        //Activa la flecha superior en la parte derecha para volver atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgPersonajeDetalle = (ImageView) findViewById(R.id.imgPersonajeDetalle);
        txtNombre = (TextView) findViewById(R.id.txtNombre);
        txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        txtFamilia = (TextView) findViewById(R.id.txtFamilia);

        c = this;

        cpd = new CircularProgressDrawable(c);
        cpd.setStrokeWidth(10f);
        cpd.setStyle(CircularProgressDrawable.LARGE);
        cpd.setCenterRadius(30f);
        cpd.start();

        //Recoge el id del personaje pulsado en la actividad anterior
        Intent i = getIntent();
        iId = i.getStringExtra("id");

        //Recoge todos los datos de la API de este personaje
        new CharacterActivity.taskConnections().execute("GET", "/Characters/"+iId);
    }

    //Esta clase se ejecuta en segundo plano y es la engargada de recoger los datos
    private class taskConnections extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            //Como en este caso usamos 'GET', se guardan los datos en 'result'
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
                    //Recogemos los datos de la API y los asignamos a variables
                    Log.d("D", "Datos: "+s);
                    JSONObject jsonObject = new JSONObject(s);
                    String nombre = jsonObject.getString("fullName");
                    String titulo = jsonObject.getString("title");
                    String familia = jsonObject.getString("family");
                    String imgUrl = jsonObject.getString("imageUrl");

                    //Glide se encarga de poner un placeholder en caso de no cargar la imagen
                    Glide.with(c).load(imgUrl).placeholder(cpd).error(R.mipmap.ic_launcher_round).into(imgPersonajeDetalle);
                    //Asignamos los datos recogidos de la API a los campos de la parte visual
                    txtNombre.setText("Nombre:    " + nombre);
                    txtTitulo.setText("Titulo:    "+ titulo);
                    txtFamilia.setText("Familia:    " + familia);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}