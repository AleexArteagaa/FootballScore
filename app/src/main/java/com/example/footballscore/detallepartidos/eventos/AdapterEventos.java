package com.example.footballscore.detallepartidos.eventos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.Response;

import java.util.List;

public class AdapterEventos extends RecyclerView.Adapter<AdapterEventos.ViewHolder>{
    private List<Response> eventos;
    public AdapterEventos(List<Response> eventos) {
        this.eventos=eventos;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdapterEventos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View eventosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_evento_individual, parent, false);
        return new AdapterEventos.ViewHolder(eventosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTiempo;
        TextView textArriba;
        TextView textAbajo;
        ImageView imageEvento;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTiempo= itemView.findViewById(R.id.tiempoEvento);
            textArriba=itemView.findViewById(R.id.textArribaEvento);
            textAbajo=itemView.findViewById(R.id.textAbajoEvento);
            imageEvento=itemView.findViewById(R.id.iconoEvento);

        }
    }
}
