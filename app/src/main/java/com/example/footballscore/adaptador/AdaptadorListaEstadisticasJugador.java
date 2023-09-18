package com.example.footballscore.adaptador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.DetallesLiga;
import com.example.footballscore.R;
import com.example.footballscore.entidades.Response;
import com.example.footballscore.entidades.partidos.estadisticas.Statistics;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdaptadorListaEstadisticasJugador extends RecyclerView.Adapter<AdaptadorListaEstadisticasJugador.ViewHolder>{

    private Context contexto;
    private ArrayList<Statistics> datosEstadisticas;
    private boolean showShimmer = true;

    public AdaptadorListaEstadisticasJugador(Context contexto) {
        this.contexto = contexto;
        datosEstadisticas = new ArrayList<>();

    }
    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdaptadorListaEstadisticasJugador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_estadisticas_jugador, parent, false);
        return new AdaptadorListaEstadisticasJugador.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaEstadisticasJugador.ViewHolder holder, int position) {

        Statistics estadistica = datosEstadisticas.get(position);

        if (estadistica.getLeague().getLogo() != null){
            holder.setImagenLogo(estadistica.getLeague().getLogo());
        }else {
            holder.imagenLogo.setImageDrawable(null);
        }

        holder.setNombreLiga(estadistica.getLeague().getName());
        System.out.println((estadistica.getLeague().getName()));



        if (estadistica.getGames().getRating() != null){
            holder.setNota(estadistica.getGames().getRating());
        }else {
            holder.textViewNota.setText(null);
            holder.textViewNota.setBackgroundResource(0);

        }

        holder.setPartidosJugados(String.valueOf(estadistica.getGames().getAppearances()));
        holder.setPartidosTitular(String.valueOf(estadistica.getGames().getLineups()));
        holder.setPartidosSuplente(String.valueOf(estadistica.getSubstitutes().getBench()));
        holder.setMinutos(String.valueOf(estadistica.getGames().getMinutes()));
        holder.setDisparosTotales(String.valueOf(estadistica.getShots().getTotal()));
        holder.setDisparosAPuerta(String.valueOf(estadistica.getShots().getOn()));
        holder.setGoles(String.valueOf(estadistica.getGoals().getTotal()));
        holder.setPasesTotales(String.valueOf(estadistica.getPasses().getTotal()));
        holder.setPasesClave(String.valueOf(estadistica.getPasses().getKey()));
        holder.setAsistencias(String.valueOf(estadistica.getGoals().getAssists()));
        holder.setAciertoPases(String.valueOf(estadistica.getPasses().getAccuracy()));
        holder.setDuelosTotales(String.valueOf(estadistica.getDuels().getTotal()));
        holder.setDuelosGanados(String.valueOf(estadistica.getDuels().getWon()));
        holder.setFaltasCometidas(String.valueOf(estadistica.getFouls().getCommitted()));
        holder.setFaltasRecibidas(String.valueOf(estadistica.getFouls().getDrawn()));
        holder.setAmarillas(String.valueOf(estadistica.getCards().getYellow()));
        holder.setDoblesAmarillas(String.valueOf(estadistica.getCards().getYellowRed()));
        holder.setRojas(String.valueOf(estadistica.getCards().getRed()));

        if (estadistica.getGames().getPosition().equalsIgnoreCase("Goalkeeper")){

            holder.textViewTituloDisparos.setText("PARADAS");
            holder.textViewTituloDisparosTotales.setText("Goles Encajados");
            holder.textViewDisparosTotales.setText(String.valueOf(estadistica.getGoals().getConceded()));
            holder.textViewTituloGoles.setText("Penaltis Parados");
            holder.textViewGoles.setText(String.valueOf(estadistica.getPenalty().getSaved()));
            holder.textViewTituloDisparosAPuerta.setText("Paradas Totales");
            holder.textViewDisparosAPuerta.setText(String.valueOf(estadistica.getGoals().getSaves()));
            holder.layoutAsistencias.setVisibility(View.GONE);
        }


    }

    public void agregarListaEstadisticas(List<Statistics> lista) {
        datosEstadisticas.clear();
        datosEstadisticas.addAll(lista);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosEstadisticas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNombreCompeticion;
        private TextView textViewPartidosJugados;
        private TextView textViewNota;
        private TextView textViewPartidosTitular;
        private TextView textViewPartidosSuplente;
        private TextView textViewMinutos;
        private TextView textViewDisparosTotales;
        private TextView textViewDisparosAPuerta;
        private TextView textViewGoles;
        private TextView textViewPasesTotales;
        private TextView textViewPasesClave;
        private TextView textViewAsistencias;
        private TextView textViewAciertoPases;
        private TextView textViewDuelosTotales;
        private TextView textViewDuelosGanados;
        private TextView textViewFaltasCometidas;
        private TextView textViewFaltasRecibidas;
        private TextView textViewAmarillas;
        private TextView textViewDoblesAmarillas;
        private TextView textViewRojas;
        private ImageView imagenLogo;
        private TextView textViewTituloDisparosTotales;
        private TextView textViewTituloDisparosAPuerta;
        private TextView textViewTituloGoles;
        private TextView textViewTituloDisparos;
        private LinearLayout layoutAsistencias;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNombreCompeticion = itemView.findViewById(R.id.textViewNombreCompeticionEstadisticas);
            textViewPartidosJugados = itemView.findViewById(R.id.textViewPartidosJugados);
            textViewNota = itemView.findViewById(R.id.textViewNotaJugadorEstadisticas);
            textViewPartidosTitular = itemView.findViewById(R.id.textViewPartidosTitular);
            textViewPartidosSuplente = itemView.findViewById(R.id.textViewPartidosSuplente);
            textViewMinutos = itemView.findViewById(R.id.textViewMinutosJugados);
            textViewDisparosTotales = itemView.findViewById(R.id.textViewDiparosTotales);
            textViewDisparosAPuerta = itemView.findViewById(R.id.textViewDiparosAPuerta);
            textViewGoles = itemView.findViewById(R.id.textViewGolesEstadisticasJugador);
            textViewPasesTotales = itemView.findViewById(R.id.textViewPasesTotalesEstadisticasJugador);
            textViewPasesClave = itemView.findViewById(R.id.textViewPasesClaveEstadisticasJugador);
            textViewAsistencias = itemView.findViewById(R.id.textViewAsistenciasEstadisticasJugador);
            textViewAciertoPases = itemView.findViewById(R.id.textViewAciertoPasesEstadisticasJugador);
            textViewDuelosTotales = itemView.findViewById(R.id.textViewDuelosTotales);
            textViewDuelosGanados = itemView.findViewById(R.id.textViewDuelosGanados);
            textViewFaltasCometidas = itemView.findViewById(R.id.textViewFaltasCometidas);
            textViewFaltasRecibidas = itemView.findViewById(R.id.textViewFaltasRecibidas);
            textViewAmarillas = itemView.findViewById(R.id.textViewTarjetasAmarillas);
            textViewDoblesAmarillas = itemView.findViewById(R.id.textViewTarjetasAmarillasDobles);
            textViewRojas = itemView.findViewById(R.id.textViewTarjetasRojas);

            imagenLogo = itemView.findViewById(R.id.imageViewCompeticionEstadisticas);

            textViewTituloDisparosTotales = itemView.findViewById(R.id.textViewTituloDiparosTotales);
            textViewTituloDisparosAPuerta = itemView.findViewById(R.id.textViewTituloDiparosAPuerta);
            textViewTituloGoles = itemView.findViewById(R.id.textViewTituloGolesEstadisticasJugador);
            textViewTituloDisparos = itemView.findViewById(R.id.textViewTituloDisparos);

            layoutAsistencias = itemView.findViewById(R.id.layoutAsistenciasEstadisticasJugador);
        }

        public void setNombreLiga(String liga) {
            textViewNombreCompeticion.setText(liga);
        }

        public void setPartidosJugados(String dato) {
            textViewPartidosJugados.setText(dato);
        }

        public void setNota(String nota) {
            double notaRedondeada = Double.parseDouble(nota);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String notaFormateada = decimalFormat.format(notaRedondeada);
            textViewNota.setText(notaFormateada);

            if (notaRedondeada < 5) {
                textViewNota.setBackgroundResource(R.drawable.red_background_nota_jugador_estadisticas);
            } else if (notaRedondeada >= 5 && notaRedondeada < 6) {
                textViewNota.setBackgroundResource(R.drawable.orange_background_nota_jugador_estadisticas);
            } else if (notaRedondeada >= 6 && notaRedondeada < 7.5) {
                textViewNota.setBackgroundResource(R.drawable.yellow_background_nota_jugador_estadisticas);
            } else if (notaRedondeada >= 7.5 && notaRedondeada <= 10) {
                textViewNota.setBackgroundResource(R.drawable.green_background_nota_jugador_estadisticas);
            }
        }

        public void setPartidosTitular(String dato) {
            textViewPartidosTitular.setText(dato);
        }

        public void setPartidosSuplente(String dato) {
            textViewPartidosSuplente.setText(dato);
        }

        public void setMinutos(String dato) {
            textViewMinutos.setText(dato);
        }

        public void setDisparosTotales(String dato) {
            textViewDisparosTotales.setText(dato);
        }

        public void setDisparosAPuerta(String dato) {
            textViewDisparosAPuerta.setText(dato);
        }

        public void setGoles(String dato) {
            textViewGoles.setText(dato);
        }

        public void setPasesTotales(String dato) {
            textViewPasesTotales.setText(dato);
        }

        public void setPasesClave(String dato) {
            textViewPasesClave.setText(dato);
        }

        public void setAsistencias(String dato) {
            textViewAsistencias.setText(dato);
        }

        public void setAciertoPases(String dato) {
            textViewAciertoPases.setText(dato);
        }

        public void setDuelosTotales(String dato) {
            textViewDuelosTotales.setText(dato);
        }

        public void setDuelosGanados(String dato) {
            textViewDuelosGanados.setText(dato);
        }

        public void setFaltasCometidas(String dato) {
            textViewFaltasCometidas.setText(dato);
        }

        public void setFaltasRecibidas(String dato) {
            textViewFaltasRecibidas.setText(dato);
        }

        public void setAmarillas(String dato) {
            textViewAmarillas.setText(dato);
        }

        public void setDoblesAmarillas(String dato) {
            textViewDoblesAmarillas.setText(dato);
        }

        public void setRojas(String dato) {
            textViewRojas.setText(dato);
        }


        public void setImagenLogo(String imagen) {
            Picasso.with(imagenLogo.getContext()).load(imagen).into(imagenLogo);
        }

    }

}
