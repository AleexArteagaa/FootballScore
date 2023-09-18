package com.example.footballscore.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.entidades.partidos.teams.Away;
import com.example.footballscore.entidades.partidos.teams.Home;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PartidosAdapter extends RecyclerView.Adapter<PartidosAdapter.ViewHolder> {

    private Context contexto;
    private ArrayList<PartidoDTO> datosPartidos;
    private Home equipoLocal;
    private Away equipoVisitante;
    private String liga;

    public PartidosAdapter(Context contexto, ArrayList<PartidoDTO> datosPartidos) {
        this.contexto = contexto;
        this.datosPartidos = datosPartidos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.partidos_detalles, parent, false);
        return new ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PartidoDTO partido = datosPartidos.get(position);
        holder.setEquipoLocal(partido.getHomeTeam());
        holder.setEquipoVisitante(partido.getAwayTeam());
        int golesLocales = datosPartidos.get(position).getHomeScore();
        int golesVisitantes = datosPartidos.get(position).getAwayScore();

        if (!datosPartidos.get(position).getEstado().equalsIgnoreCase("Match Finished")){

            if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Not Started")){
                String fechaHora = datosPartidos.get(position).getDate();
                String hora = fechaHora.substring(11, 16);
                System.out.println(datosPartidos.get(position).getDate());
                holder.setResultado(hora);
                holder.setEstadoPartido("");
            } else if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Match Abandoned")) {
                holder.setEstadoPartido("APL");
                holder.estadoPartido.setBackgroundColor(Color.RED);
                holder.estadoPartido.setTextColor(Color.WHITE);
                String fechaHora = datosPartidos.get(position).getDate();
                String hora = fechaHora.substring(11, 16);
                holder.setResultado(hora);

            } else if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Halftime")) {
                holder.setEstadoPartido("DES");
                holder.setResultado(golesLocales + " - " + golesVisitantes);
                holder.estadoPartido.setBackgroundColor(Color.parseColor("#047709"));
                holder.estadoPartido.setTextColor(Color.WHITE);
            } else {
                holder.setEstadoPartido(datosPartidos.get(position).getElapsed() + "'");
                holder.setResultado(golesLocales + " - " + golesVisitantes);
                holder.estadoPartido.setBackgroundColor(Color.parseColor("#047709"));
                holder.estadoPartido.setTextColor(Color.WHITE);
            }


        }else {
            holder.setEstadoPartido("FIN");
            holder.setResultado(golesLocales + " - " + golesVisitantes);
            holder.estadoPartido.setBackgroundColor(Color.GRAY);
            holder.estadoPartido.setTextColor(Color.WHITE);

        }

        String jornadaTxt = datosPartidos.get(position).getRound();
        String numero;
        if (jornadaTxt.startsWith("Regular Season")) {
            numero = "Jornada " + jornadaTxt.substring(16);
        } else {
            numero = jornadaTxt;
        }
        holder.setJornada(numero);
        holder.setImagenEquipoLocal(datosPartidos.get(position).getHomeLogo());
        holder.setImagenEquipoVisitante(datosPartidos.get(position).getAwayLogo());

    }

    @Override
    public int getItemCount() {
        return datosPartidos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView equipoLocal;
        private TextView equipoVisitante;
        private TextView resultado;
        private TextView jornada;
        private ImageView imagenEquipoLocal;
        private ImageView imagenEquipoVisitante;
        private TextView estadoPartido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            equipoLocal = itemView.findViewById(R.id.textViewEquipoCasa);
            equipoVisitante = itemView.findViewById(R.id.textViewEquipoFuera);
            resultado = itemView.findViewById(R.id.textViewDetallesResultado);
            imagenEquipoLocal = itemView.findViewById(R.id.imageViewEquipoCasa);
            imagenEquipoVisitante = itemView.findViewById(R.id.imageViewEquipoFuera);
            jornada = itemView.findViewById(R.id.textViewRonda);
            estadoPartido = itemView.findViewById(R.id.textViewEstado);
        }

        public void setEquipoLocal(String nombreEquipo) {
            equipoLocal.setText(nombreEquipo);
        }

        public void setEquipoVisitante(String nombreEquipo) {
            equipoVisitante.setText(nombreEquipo);
        }

        public void setResultado(String resultadoPartido) {
            resultado.setText(resultadoPartido);
        }

        public void setJornada(String jornadaText) {
            jornada.setText(jornadaText);
        }

        public void setImagenEquipoLocal(String imagen) {
            Picasso.with(imagenEquipoLocal.getContext()).load(imagen).into(imagenEquipoLocal);
        }

        public void setImagenEquipoVisitante(String imagen) {
            Picasso.with(imagenEquipoVisitante.getContext()).load(imagen).into(imagenEquipoVisitante);
        }

        public void setEstadoPartido(String estado) {
            estadoPartido.setText(estado);
        }
    }
}
