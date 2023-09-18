package com.example.footballscore.adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.R;
import com.example.footballscore.entidades.Fichaje;
import com.example.footballscore.entidades.Response;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.jugadores.TemporadasResponse;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AdaptadorListaFichajes extends RecyclerView.Adapter<AdaptadorListaFichajes.ViewHolder>{

    private ArrayList<Fichaje> datosFichajes;
    private Context contexto;


    public AdaptadorListaFichajes(Context context, ArrayList<Fichaje> jugadores){
        this.contexto = context;
        datosFichajes = jugadores;

    }


    @NonNull
    @Override
    public AdaptadorListaFichajes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_fichaje, parent, false);
        return new AdaptadorListaFichajes.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaFichajes.ViewHolder holder, int position) {

        int id = datosFichajes.get(position).getId();

        holder.setNombreJugador(datosFichajes.get(position).getJugador());

        holder.setImagenEquipoAlQueVa(datosFichajes.get(position).getLogoEquipoAlQueVa());
        holder.setNombreEquipoAlQueViene(datosFichajes.get(position).getEquipoAlQueVa());
        holder.setImagenEquipoDelQueViene(datosFichajes.get(position).getLogoEquipoDelQueViene());
        holder.setNombreEquipoDelQueViene(datosFichajes.get(position).getEquipoDelQueViene());

        if (datosFichajes.get(position).getTipo() == null){

        } else if (datosFichajes.get(position).getTipo().equalsIgnoreCase("Loan")) {
            holder.setInfoFichaje("Cesión");
        } else if (datosFichajes.get(position).getTipo().equalsIgnoreCase("N/A")) {
            holder.setInfoFichaje("Libre");
        } else if (datosFichajes.get(position).getTipo().contains("Free")) {
            holder.setInfoFichaje("Gratis");
        } else if (datosFichajes.get(position).getTipo().contains("€")){
            String tipo = datosFichajes.get(position).getTipo();
            String[] partes = tipo.split(" ");
            if (partes.length == 2) {
                String cantidad = partes[1];
                String moneda = partes[0];
                String nuevoTipo = cantidad + " " + moneda;
                holder.setInfoFichaje(nuevoTipo);
            } else {
                holder.setInfoFichaje(tipo);
            }        }

        Picasso.with(holder.imagenJugador.getContext()).load("https://media-2.api-sports.io/football/players/" + id + ".png").into(holder.imagenJugador);


    }



    @Override
    public int getItemCount() {
        return datosFichajes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewJugador;
        private TextView textViewInformacionFichaje;
        private TextView textViewEquipoDelQueViene;
        private TextView textViewEquipoAlQueVa;
        private ImageView imagenJugador;
        private ImageView imagenEquipoDelQueViene;
        private ImageView imagenEquipoAlQueVa;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewJugador = itemView.findViewById(R.id.textViewNombreJugadorFichaje);
            textViewInformacionFichaje = itemView.findViewById(R.id.textViewInformacionFichaje);
            textViewEquipoDelQueViene = itemView.findViewById(R.id.textViewEquipoDelQueVieneFichaje);
            textViewEquipoAlQueVa = itemView.findViewById(R.id.textViewEquipoAlQueVieneFichaje);
            imagenJugador = itemView.findViewById(R.id.imageViewJugadorFichaje);
            imagenEquipoDelQueViene = itemView.findViewById(R.id.imageViewEquipoDelQueVieneFichaje);
            imagenEquipoAlQueVa = itemView.findViewById(R.id.imageViewEquipoAlQueVieneFichaje);

        }

        public void setNombreJugador(String nombreJugador) {
            textViewJugador.setText(nombreJugador);
        }
        public void setInfoFichaje(String info) {
            textViewInformacionFichaje.setText(info);
        }

        public void setNombreEquipoDelQueViene(String equipo) {
            textViewEquipoDelQueViene.setText(equipo);
        }
        public void setNombreEquipoAlQueViene(String equipo) {
            textViewEquipoAlQueVa.setText(equipo);
        }

        public void setImagenJugador(String imagen) {
            Picasso.with(imagenJugador.getContext()).load(imagen).into(imagenJugador);
        }
        public void setImagenEquipoDelQueViene(String imagen) {
            Picasso.with(imagenEquipoDelQueViene.getContext()).load(imagen).into(imagenEquipoDelQueViene);
        }
        public void setImagenEquipoAlQueVa(String imagen) {
            Picasso.with(imagenEquipoAlQueVa.getContext()).load(imagen).into(imagenEquipoAlQueVa);
        }


    }
}
