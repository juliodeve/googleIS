package com.vazquez.julio.googleis.ADAPTERS;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vazquez.julio.googleis.ACTIVITYS.DetallesTrayectoActivity;
import com.vazquez.julio.googleis.POJO.Trayecto;
import com.vazquez.julio.googleis.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 12/02/2018.
 */

public class TrayectoAdapter extends RecyclerView.Adapter<TrayectoAdapter.ViewHolder> {
    List<Trayecto> usuarioList = new ArrayList<>();
    Context context;

    public TrayectoAdapter (Context context, List<Trayecto> usuarioList){
        this.context = context;
        this.usuarioList = usuarioList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DecimalFormat objFormato = new DecimalFormat("#.###");
        holder.velocidad.setText("Velocidad: "+objFormato.format(usuarioList.get(position).getVelocidad())+"km/h");
        holder.distancia.setText("Distancia: "+objFormato.format(usuarioList.get(position).getDistancia())+"Km");
        holder.duracion.setText("Duracion: "+usuarioList.get(position).getDuracion()+"Min");
        //Glide.with(context).load(global.photo).into(holder.photo);
        holder.fecha.setText("Fecha: "+usuarioList.get(position).getFecha());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                ArrayList<Trayecto> trayectos = new ArrayList<Trayecto>();
                Trayecto trayecto = usuarioList.get(position);
                trayecto.setTrayectos(trayectos);
                Intent intent = new Intent(context, DetallesTrayectoActivity.class);
                intent.putExtra("trajet", usuarioList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView fecha;
        ImageView photo;
        TextView duracion;
        TextView distancia;
        TextView velocidad;
        CardView cardView;
        private ItemClickListener clickListener;

        public ViewHolder (View item) {
            super(item);
            cardView = (CardView) item.findViewById(R.id.cardView);
            fecha = (TextView) item.findViewById(R.id.txtFechaI);
            velocidad = (TextView) item.findViewById(R.id.txtVelocidadI);
            duracion = (TextView) item.findViewById(R.id.txtDuracionI);
            photo = item.findViewById(R.id.img1I);
            distancia = (TextView) item.findViewById(R.id.txtDistanciaI);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), false);
            return true;
        }
    }
}
