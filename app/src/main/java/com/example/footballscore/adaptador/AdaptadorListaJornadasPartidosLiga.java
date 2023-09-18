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
import com.example.footballscore.entidades.JornadaDTO;
import com.example.footballscore.entidades.PartidoDTO;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaJornadasPartidosLiga extends RecyclerView.Adapter<AdaptadorListaJornadasPartidosLiga.ViewHolder>{

    private ArrayList<JornadaDTO> datosPartidos;
    private Context contexto;

    public AdaptadorListaJornadasPartidosLiga(Context context){
        this.contexto = context;
        this.datosPartidos = new ArrayList<>();

    }

    public void agregarListaPosiciones(List<JornadaDTO> lista) {

        datosPartidos.addAll(lista);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdaptadorListaJornadasPartidosLiga.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_partidos_por_mes, parent, false);
        return new AdaptadorListaJornadasPartidosLiga.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaJornadasPartidosLiga.ViewHolder holder, int position) {

        JornadaDTO fichajesPorJornada = datosPartidos.get(position);


        String jornada = fichajesPorJornada.getJornada();
        String numero;
        if (jornada.startsWith("Regular Season")) {
            numero = "Jornada " + jornada.substring(16);
        } else {
            numero = jornada;
        }
        holder.setJornada(numero);

        ArrayList<PartidoDTO> partidos = fichajesPorJornada.getPartidos();

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(contexto));
        AdaptadorListaPartidosPorMes adaptadorListaPartidosPorMes = new AdaptadorListaPartidosPorMes(contexto, partidos);
        holder.recyclerView.setAdapter(adaptadorListaPartidosPorMes);

    }



    @Override
    public int getItemCount() {
        return datosPartidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewMes;

        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewMes = itemView.findViewById(R.id.textViewMesPartidos);
            recyclerView = itemView.findViewById(R.id.recyclerViewPartidosPorMes);
        }

        public void setJornada(String jornada) {
            textViewMes.setText(jornada);
        }



    }
}
