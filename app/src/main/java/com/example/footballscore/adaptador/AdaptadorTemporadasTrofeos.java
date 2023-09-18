package com.example.footballscore.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.TemporadaDTO;
import com.example.footballscore.entidades.TrofeoDTO;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorTemporadasTrofeos extends RecyclerView.Adapter<AdaptadorTemporadasTrofeos.ViewHolder>{

    private Context contexto;
    private ArrayList<TemporadaDTO> datosTemporadas;
    private boolean showShimmer = true;


    public AdaptadorTemporadasTrofeos(Context contexto) {
        this.contexto = contexto;
        datosTemporadas = new ArrayList<>();

    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorTemporadasTrofeos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_temporadas_trofeos, parent, false);
        return new AdaptadorTemporadasTrofeos.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTemporadasTrofeos.ViewHolder holder, int position) {

        TemporadaDTO temporada = datosTemporadas.get(position);

        holder.setTemporada(temporada.getTemporada());

        ArrayList<TrofeoDTO> trofeos = temporada.getTrofeos();

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(contexto));
        AdaptadorTrofeosJugador adaptador = new AdaptadorTrofeosJugador(contexto, trofeos);
        holder.recyclerView.setAdapter(adaptador);


    }

    public void agregarListaTemporadas(List<TemporadaDTO> lista) {

        datosTemporadas.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosTemporadas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTemporada;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTemporada = itemView.findViewById(R.id.textViewTemporadasTrofeos);
            recyclerView = itemView.findViewById(R.id.recyclerViewTrofeosPorTemporada);

        }

        public void setTemporada(String fecha) {
            textViewTemporada.setText(fecha);
        }



    }
}
