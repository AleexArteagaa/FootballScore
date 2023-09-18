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
import com.example.footballscore.Traductor;
import com.example.footballscore.entidades.JugadorDTO;
import com.example.footballscore.entidades.PosicionDTO;

import java.util.ArrayList;

public class AdaptadorListaPosiciones extends RecyclerView.Adapter<AdaptadorListaPosiciones.ViewHolder>{

    private ArrayList<PosicionDTO> datosPosiciones;
    private Context contexto;

    public AdaptadorListaPosiciones(Context context, ArrayList<PosicionDTO> datosPosiciones){
        this.contexto = context;
        this.datosPosiciones = datosPosiciones;

    }


    @NonNull
    @Override
    public AdaptadorListaPosiciones.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_posicion_jugador, parent, false);
        return new AdaptadorListaPosiciones.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaPosiciones.ViewHolder holder, int position) {

        PosicionDTO posicionDTO = datosPosiciones.get(position);
        Traductor traductor = new Traductor();

        holder.setPosicion(traductor.traducir(posicionDTO.getPosicion()));

        ArrayList<JugadorDTO> jugadores = posicionDTO.getJugadores();

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(contexto));
        AdaptadorListaJugadores adaptadorListaJugadores = new AdaptadorListaJugadores(contexto, jugadores);
        holder.recyclerView.setAdapter(adaptadorListaJugadores);

    }



    @Override
    public int getItemCount() {
        if (datosPosiciones == null){
            return 0;
        }else {
            return datosPosiciones.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewPosicion;

        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPosicion = itemView.findViewById(R.id.textViewPosicionJugador);
            recyclerView = itemView.findViewById(R.id.recyclerViewJugadoresPorPosicion);
        }

        public void setPosicion(String posicion) {
            textViewPosicion.setText(posicion);
        }



    }
}
