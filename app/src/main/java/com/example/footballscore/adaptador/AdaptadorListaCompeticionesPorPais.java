package com.example.footballscore.adaptador;

import android.content.Context;
import android.content.Intent;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdaptadorListaCompeticionesPorPais extends RecyclerView.Adapter<AdaptadorListaCompeticionesPorPais.ViewHolder>{

    private Context contexto;
    private ArrayList<Response> datosPaises;
    private boolean showShimmer = true;

    public AdaptadorListaCompeticionesPorPais(Context contexto) {
        this.contexto = contexto;
        datosPaises = new ArrayList<>();

    }
    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdaptadorListaCompeticionesPorPais.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_competiciones_por_pais, parent, false);
        return new AdaptadorListaCompeticionesPorPais.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaCompeticionesPorPais.ViewHolder holder, int position) {

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

            String name = datosPaises.get(position).getLeague().getName();
            String logo = datosPaises.get(position).getLeague().getLogo();
            int ligaId = datosPaises.get(position).getLeague().getId();


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
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetallesLiga.class);
                    intent.putExtra("liga", ligaId);
                    view.getContext().startActivity(intent);

                }
            });
        }

    }

    public void agregarListaPaises(List<Response> lista) {
        datosPaises.addAll(lista);

        Collections.sort(datosPaises, new Comparator<Response>() {
            @Override
            public int compare(Response pais1, Response pais2) {
                int idLiga1 = pais1.getLeague().getId();
                int idLiga2 = pais2.getLeague().getId();

                return Integer.compare(idLiga1, idLiga2);
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return datosPaises.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreCompeticion;

        private ImageView imagenLogo;
        private ShimmerFrameLayout shimmerLayout;
        private LinearLayout layout;
        Handler handler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombreCompeticion = itemView.findViewById(R.id.textViewNombreCompeticion);
            imagenLogo = itemView.findViewById(R.id.imageViewLogoCompeticion);
            shimmerLayout = itemView.findViewById(R.id.shimmer_view_competiciones_por_pais);
            layout = itemView.findViewById(R.id.layoutCompeticionesPorPais);
        }

        public void setNombreLiga(String liga) {
            nombreCompeticion.setText(liga);
        }

        public void setImagenLogo(String imagen) {
            Picasso.with(imagenLogo.getContext()).load(imagen).into(imagenLogo);
        }

    }

}
