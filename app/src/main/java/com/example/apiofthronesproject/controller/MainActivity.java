package com.example.apiofthronesproject.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apiofthronesproject.R;
import com.example.apiofthronesproject.adapter.RecyclerAdapter;
import com.example.apiofthronesproject.io.HttpConnectAOT;
import com.example.apiofthronesproject.model.Personaje;
import com.example.apiofthronesproject.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerPersonajes;
    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new RecyclerAdapter(this);

        new taskConnections().execute("GET", "/Characters");

        recyclerPersonajes = (RecyclerView) findViewById(R.id.recView);
        recyclerPersonajes.setLayoutManager(new LinearLayoutManager(this));
        recyclerPersonajes.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CharacterActivity.class);
                i.putExtra("id", adapter.listPersonajes.get(recyclerPersonajes.getChildAdapterPosition(view)).getId());
                startActivity(i);
                view.setSelected(true);
            }
        });
    }


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
}