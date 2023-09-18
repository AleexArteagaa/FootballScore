package com.example.footballscore.adaptador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.detallepartidos.TabbedActivityDetallePartido;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.R;
import com.example.footballscore.entidades.partidos.teams.Away;
import com.example.footballscore.entidades.partidos.teams.Home;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorListaPartidos extends RecyclerView.Adapter<AdaptadorListaPartidos.ViewHolder> {

    private ArrayList<PartidoDTO> datosPartidos;
    private boolean showShimmer;
    private Context context;

    public AdaptadorListaPartidos(Context contexto, ArrayList<PartidoDTO> datosPartidos) {
        this.datosPartidos = datosPartidos;
        this.context= contexto;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_partido_menu_principal, parent, false);
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

                    listenerDetalle(holder, partido, true);

                } else if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Match Abandoned")) {
                    holder.setEstadoPartido("APL");
                    holder.estadoPartido.setBackgroundColor(Color.RED);
                    holder.estadoPartido.setTextColor(Color.WHITE);
                    String fechaHora = datosPartidos.get(position).getDate();
                    String hora = fechaHora.substring(11, 16);
                    holder.setResultado(hora);

                    listenerDetalle(holder, partido, true);

                } else if (datosPartidos.get(position).getEstado().equalsIgnoreCase("Halftime")) {
                    holder.setEstadoPartido("DES");
                    holder.setResultado(golesLocales + " - " + golesVisitantes);
                    holder.estadoPartido.setBackgroundColor(Color.parseColor("#047709"));
                    holder.estadoPartido.setTextColor(Color.WHITE);

                    listenerDetalle(holder, partido, false);
                }else if(datosPartidos.get(position).getEstado().equalsIgnoreCase("Time to be defined")){

                    String fechaHora = datosPartidos.get(position).getDate();
                    String hora = fechaHora.substring(11, 16);
                    System.out.println(datosPartidos.get(position).getDate());
                    holder.setResultado(hora);
                    holder.setEstadoPartido("");

                    listenerDetalle(holder, partido, false);

                }else {
                    holder.setEstadoPartido(datosPartidos.get(position).getElapsed() + "'");
                    holder.setResultado(golesLocales + " - " + golesVisitantes);
                    holder.estadoPartido.setBackgroundColor(Color.parseColor("#047709"));
                    holder.estadoPartido.setTextColor(Color.WHITE);

                    listenerDetalle(holder, partido, false);

                }

            }else {
                holder.setEstadoPartido("FIN");
                holder.setResultado(golesLocales + " - " + golesVisitantes);
                holder.estadoPartido.setBackgroundColor(Color.GRAY);
                holder.estadoPartido.setTextColor(Color.WHITE);

                listenerDetalle(holder, partido, false);

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

    private void listenerDetalle(@NonNull ViewHolder holder, PartidoDTO partido, boolean isNotStarted) {
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, TabbedActivityDetallePartido.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("partido", partido);
                bundle.putBoolean("isNotStarted", isNotStarted);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
        holder.equipoLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, TabbedActivityDetallePartido.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("partido", partido);
                bundle.putBoolean("isNotStarted", isNotStarted);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
        holder.equipoVisitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, TabbedActivityDetallePartido.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("partido", partido);
                bundle.putBoolean("isNotStarted", isNotStarted);
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
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

        private LinearLayout content;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            equipoLocal = itemView.findViewById(R.id.textViewHomeTeam);
            equipoVisitante = itemView.findViewById(R.id.textViewAwayTeam);
            resultado = itemView.findViewById(R.id.textViewResultadoDetalles);
            imagenEquipoLocal = itemView.findViewById(R.id.imageViewHomeTeam);
            imagenEquipoVisitante = itemView.findViewById(R.id.imageViewAwayTeam);
            jornada = itemView.findViewById(R.id.textViewRound);
            estadoPartido = itemView.findViewById(R.id.textViewState);
            content= itemView.findViewById(R.id.contentPartido);

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
        public void setEstadoPartido(String estado){
            estadoPartido.setText(estado);
        }

    }
}