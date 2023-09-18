package com.example.footballscore.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.TrayectoriaDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AdaptadorTrayectoriaJugador extends RecyclerView.Adapter<AdaptadorTrayectoriaJugador.ViewHolder>{

    private Context contexto;
    private ArrayList<TrayectoriaDTO> datosTrayectoria;
    private boolean showShimmer = true;


    public AdaptadorTrayectoriaJugador(Context contexto) {
        this.contexto = contexto;
        datosTrayectoria = new ArrayList<>();

    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorTrayectoriaJugador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_trayectoria_jugador, parent, false);
        return new AdaptadorTrayectoriaJugador.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTrayectoriaJugador.ViewHolder holder, int position) {

        TrayectoriaDTO trayectoria = datosTrayectoria.get(position);

        holder.setImagenLogo(trayectoria.getLogoEquipo());
        holder.setNombreEquipo(trayectoria.getEquipo());
        holder.setTextViewTemporada(trayectoria.getTemporada() + "/" + (trayectoria.getTemporada() +1) );


    }

    public void agregarListaEstadisticas(List<TrayectoriaDTO> lista) {
        Collections.reverse(lista);

        datosTrayectoria.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosTrayectoria.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreEquipo;
        private TextView textViewTemporada;


        private ImageView imagenEquipo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreEquipo = itemView.findViewById(R.id.textViewNombreEquipoTrayectoriaJugador);
            imagenEquipo = itemView.findViewById(R.id.imageViewEquipoTrayectoriaJugador);
            textViewTemporada = itemView.findViewById(R.id.textViewTemporadaTrayectoriaJugador);

        }

        public void setNombreEquipo(String equipo) {
            nombreEquipo.setText(equipo);
        }

        public void setTextViewTemporada(String temporada) {
            textViewTemporada.setText(temporada);
        }


        public void setImagenLogo(String imagen) {
            Picasso.with(imagenEquipo.getContext()).load(imagen).into(imagenEquipo);
        }


    }
}
