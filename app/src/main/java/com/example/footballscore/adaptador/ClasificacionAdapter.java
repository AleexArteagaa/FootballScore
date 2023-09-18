package com.example.footballscore.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.clasificaciones.Standings;

import java.util.ArrayList;
import java.util.List;

public class ClasificacionAdapter extends RecyclerView.Adapter<ClasificacionAdapter.ViewHolder> {

    private Context contexto;
    private List<Standings> datosClasificacion;


    public ClasificacionAdapter(Context contexto) {
        this.contexto = contexto;
        this.datosClasificacion=new ArrayList<>();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View clasificacionLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_clasificacion_detalles, parent, false);
        return new ViewHolder(clasificacionLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Standings standing = datosClasificacion.get(position);

        if (standing.getRank() == 1){
            holder.layoutClasificacion.setBackgroundColor(Color.parseColor("#7ecf8f"));
        }else if (standing.getDescription() != null && (standing.getDescription().contains("Champions League") || standing.getDescription().contains("World Cup") || standing.getDescription().contains("Libertadores"))){
            holder.layoutClasificacion.setBackgroundColor(Color.parseColor("#7eaccf"));
        }else if (standing.getDescription() != null && (standing.getDescription().contains("Champions League") || standing.getDescription().contains("Europa League"))){
            holder.layoutClasificacion.setBackgroundColor(Color.parseColor("#edbd6f"));
        }else if (standing.getDescription() != null && standing.getDescription().contains("Europa League")){
            holder.layoutClasificacion.setBackgroundColor(Color.parseColor("#edbd6f"));
        }else if (standing.getDescription() != null && standing.getDescription().contains("Europa Conference League")){
            holder.layoutClasificacion.setBackgroundColor(Color.parseColor("#95e8c4"));
        }else if (standing.getDescription() != null && standing.getDescription().contains("Relegation")){
            holder.layoutClasificacion.setBackgroundColor(Color.parseColor("#eb6565"));
        } else if (standing.getDescription() == null) {
            holder.layoutClasificacion.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.setEquipo(standing.getTeam().getName());
        holder.setPuntos(standing.getPoints());
        holder.setPosicionEquipo(standing.getRank());
        holder.setPartidosJugados(standing.getAll().getPlayed());
        holder.setPartidosGanados(standing.getAll().getWin());
        holder.setPartidosEmpatados(standing.getAll().getDraw());
        holder.setPartidosPerdidos(standing.getAll().getLose());




    }

    @Override
    public int getItemCount() {
        return datosClasificacion.size();
    }

    public void agregarListaClasificacion(List<Standings> lista) {
        datosClasificacion.clear();
        datosClasificacion.addAll(lista);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView equipo;
        private TextView puntos;
        private TextView posicionEquipo;
        private RecyclerView recyclerView;
        private TextView partidosJugados;
        private TextView partidosGanados;
        private TextView partidosEmpatados;
        private TextView partidosPerdidos;
        private CardView layoutClasificacion;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView=itemView.findViewById(R.id.recyclerClasificacion);
            layoutClasificacion =itemView.findViewById(R.id.layoutClasificacionEquipo);
            equipo = itemView.findViewById(R.id.textView2);
            puntos = itemView.findViewById(R.id.textView3);
            posicionEquipo = itemView.findViewById(R.id.textViewEquipo);
            partidosJugados = itemView.findViewById(R.id.textView4);
            partidosGanados = itemView.findViewById(R.id.textView5);
            partidosEmpatados = itemView.findViewById(R.id.textView6);
            partidosPerdidos = itemView.findViewById(R.id.textView7);


        }



        public void setEquipo(String nombreEquipo) {
            equipo.setText(nombreEquipo);
        }

        public void setPuntos(int puntosEquipo) {
            puntos.setText(String.valueOf(puntosEquipo));
        }

        public void setPosicionEquipo(int Posicion) {
            posicionEquipo.setText(String.valueOf(Posicion));
        }



        public void setPartidosJugados(int partidosJugado) {
            partidosJugados.setText(String.valueOf(partidosJugado));
        }

        public void setPartidosGanados(int partidosGanado) {
            partidosGanados.setText(String.valueOf(partidosGanado));
        }

        public void setPartidosEmpatados(int partidosEmpatado) {
            partidosEmpatados.setText(String.valueOf(partidosEmpatado));
        }

        public void setPartidosPerdidos(int partidosPerdido) {
            partidosPerdidos.setText(String.valueOf(partidosPerdido));
        }
    }
}
