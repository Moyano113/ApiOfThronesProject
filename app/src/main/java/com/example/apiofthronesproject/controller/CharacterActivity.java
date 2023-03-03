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

        Intent i = getIntent();
        iId = i.getStringExtra("id");

        new CharacterActivity.taskConnections().execute("GET", "/Characters/"+iId);
    }

    private class taskConnections extends AsyncTask<String,Void,String>{

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
                    Log.d("D", "Datos: "+s);
                    JSONObject jsonObject = new JSONObject(s);
                    String nombre = jsonObject.getString("fullName");
                    String titulo = jsonObject.getString("title");
                    String familia = jsonObject.getString("family");
                    String imgUrl = jsonObject.getString("imageUrl");

                    Glide.with(c).load(imgUrl).placeholder(cpd).error(R.mipmap.ic_launcher_round).into(imgPersonajeDetalle);
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