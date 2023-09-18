package com.example.footballscore.adaptador;

import android.content.Context;
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

import com.example.footballscore.R;
import com.example.footballscore.entidades.Response;
import com.example.footballscore.entidades.partidos.league.League;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdaptadorListaCompeticionesPorEquipo extends RecyclerView.Adapter<AdaptadorListaCompeticionesPorEquipo.ViewHolder> {

    private Context contexto;
    private ArrayList<League> datosCompeticiones;
    private boolean showShimmer = true;

    public AdaptadorListaCompeticionesPorEquipo(Context contexto) {
        this.contexto = contexto;
        datosCompeticiones = new ArrayList<>();

    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorListaCompeticionesPorEquipo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_competiciones_equipo, parent, false);
        return new AdaptadorListaCompeticionesPorEquipo.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaCompeticionesPorEquipo.ViewHolder holder, int position) {

        if (showShimmer) {
            holder.shimmerLayout.startShimmer();
            int startColor = ContextCompat.getColor(contexto, R.color.shimmer_background_start);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(startColor);
            gradientDrawable.setCornerRadius(20);
            holder.shimmerLayout.setBackground(gradientDrawable);

            final Handler handler = new Handler();
            final String[] loadingTexts = {"Cargando.", "Cargando..", "Cargando..."};
            final TextView textView = holder.nombreCompeticion;

            Runnable runnable = new Runnable() {
                int index = 0;

                @Override
                public void run() {
                    textView.setText(loadingTexts[index]);
                    index = (index + 1) % loadingTexts.length;
                    handler.postDelayed(this, 600);
                }
            };

            handler.post(runnable);
            holder.handler = handler;
        }else {

            String name = datosCompeticiones.get(position).getName();
            String logo = datosCompeticiones.get(position).getLogo();

            holder.setNombreLiga(name);
            holder.setImagenLogo(logo);

            holder.shimmerLayout.stopShimmer();
            holder.shimmerLayout.setShimmer(null);
            holder.shimmerLayout.setBackground(null);
            holder.layout.setBackgroundResource(R.drawable.paises_background);

            if (holder.handler != null) {
                holder.handler.removeCallbacksAndMessages(null);
                holder.handler = null;
            }
        }



    }

    public void agregarListaCompeticiones(List<League> lista) {
        datosCompeticiones.addAll(lista);
        Collections.sort(datosCompeticiones, new Comparator<League>() {
            @Override
            public int compare(League liga1, League liga2) {
                int idLiga1 = liga1.getId();
                int idLiga2 = liga2.getId();

                return Integer.compare(idLiga1, idLiga2);
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosCompeticiones.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreCompeticion;

        private ImageView imagenLogo;
        private ShimmerFrameLayout shimmerLayout;
        private LinearLayout layout;
        private Handler handler;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreCompeticion = itemView.findViewById(R.id.textViewNombreCompeticionEquipo);
            imagenLogo = itemView.findViewById(R.id.imageViewLogoCompeticionEquipo);
            shimmerLayout = itemView.findViewById(R.id.shimmer_view_competiciones_por_equipo);
            layout = itemView.findViewById(R.id.layoutCompeticionesPorEquipo);
        }

        public void setNombreLiga(String liga) {
            nombreCompeticion.setText(liga);
        }

        public void setImagenLogo(String imagen) {
            Picasso.with(imagenLogo.getContext()).load(imagen).into(imagenLogo);
        }

    }

}
