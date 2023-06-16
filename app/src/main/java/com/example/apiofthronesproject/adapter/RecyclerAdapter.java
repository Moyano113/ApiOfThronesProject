package com.example.apiofthronesproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Placeholder;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.apiofthronesproject.R;
import com.example.apiofthronesproject.model.Personaje;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolderPersonajes> {
    public ArrayList<Personaje> listPersonajes;
    private Context c;
    private CircularProgressDrawable cpd;
    View.OnClickListener onClickListener;

    //Constructor de la clase
    public RecyclerAdapter(Context c) {
        this.c = c;
        this.listPersonajes = new ArrayList();
    }

    //Método encargado de añadir un nuevo personaje a la lista
    public void addPersonaje(Personaje p){
        listPersonajes.add(p);
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolderPersonajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Asigno el view holder al que he personalizado abajo
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_personajes,parent,false);

        v.setOnClickListener(onClickListener);
        return new ViewHolderPersonajes(v);
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPersonajes holder, int position) {
        //Cargo el CircularProgressDrawable y lo configuro para que aparezca un circulito dando vueltas
        //mientras se carga el elemento de la lista
        cpd = new CircularProgressDrawable(c);
        cpd.setStrokeWidth(10f);
        cpd.setStyle(CircularProgressDrawable.LARGE);
        cpd.setCenterRadius(30f);
        cpd.start();

        //Asigno el campo nombre al elemento de la vista txtNombre
        Personaje p = listPersonajes.get(position);
        holder.txtNombre.setText(p.getNombre());
        Glide.with(c).load(p.getImgUrl()).placeholder(cpd).error(R.mipmap.ic_launcher_round).into(holder.imgPersonaje);
    }

    @Override
    public int getItemCount() {
        return listPersonajes.size();
    }

    public class ViewHolderPersonajes extends RecyclerView.ViewHolder{

        //Asigno la parte gráfica en donde se cargarán los datos del recycler view
        TextView txtNombre;
        ImageView imgPersonaje;

        public ViewHolderPersonajes(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.lblNombre);
            imgPersonaje = (ImageView) itemView.findViewById(R.id.imgPersonaje);
        }
    }
}
