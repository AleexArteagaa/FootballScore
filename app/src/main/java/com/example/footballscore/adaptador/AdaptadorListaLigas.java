package com.example.footballscore.adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.DetalleEquipos;
import com.example.footballscore.DetallesLiga;
import com.example.footballscore.entidades.LigaDTO;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AdaptadorListaLigas extends RecyclerView.Adapter<AdaptadorListaLigas.ViewHolder> {

    private Context contexto;
    private List<LigaDTO> listaLigas;
    private boolean showShimmer = true;




    public AdaptadorListaLigas(Context contexto) {
        this.contexto = contexto;
        listaLigas = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ligasLayout = LayoutInflater.from(contexto).inflate(R.layout.vista_liga_menu_principal, parent, false);

        return new ViewHolder(ligasLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (showShimmer) {
            holder.shimmer_view_container.startShimmer();
            int startColor = ContextCompat.getColor(contexto, R.color.shimmer_background_start);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(startColor);
            gradientDrawable.setCornerRadius(20);
            holder.shimmer_view_container.setBackground(gradientDrawable);

            final Handler handler = new Handler();
            final String[] loadingTexts = {"Cargando.", "Cargando..", "Cargando..."};
            final TextView textView = holder.nombreLiga;

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
        } else {
            LigaDTO liga = listaLigas.get(position);
            int ligaID = listaLigas.get(position).getId();
            holder.setNombreLiga(liga.getName());
            holder.setImagenLiga(liga.getLogo());
            holder.setImagenBanderaLigaLiga(liga.getFlag());

            ArrayList<PartidoDTO> partidosDeLaLiga = liga.getPartidos();

            holder.recyclerViewPartidos.setLayoutManager(new LinearLayoutManager(contexto));
            AdaptadorListaPartidos adaptadorListaPartidos = new AdaptadorListaPartidos(contexto, partidosDeLaLiga);
            holder.recyclerViewPartidos.setAdapter(adaptadorListaPartidos);

            holder.shimmer_view_container.stopShimmer();
            holder.shimmer_view_container.setShimmer(null);
            holder.shimmer_view_container.setBackground(null);
            holder.layout.setBackgroundResource(R.drawable.rounded_border);

            if (holder.handler != null) {
                holder.handler.removeCallbacksAndMessages(null);
                holder.handler = null;
            }
            holder.nombreLiga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetallesLiga.class);
                    intent.putExtra("liga", ligaID);
                    view.getContext().startActivity(intent);
                }
            });

            holder.layoutLiga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetallesLiga.class);
                    intent.putExtra("liga", ligaID);
                    view.getContext().startActivity(intent);
                }
            });
        }


    }
    @Override
    public int getItemCount() {
        return showShimmer ? 5 : listaLigas.size();
    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    public void clear(){
        listaLigas.clear();
        notifyDataSetChanged();
    }

    public void agregarListaLigas(ArrayList<LigaDTO> lista) {
        listaLigas.addAll(lista);

        List<Integer> prioridad = Arrays.asList(1, 2, 3,4, 6,9,960,850,10,14,15, 531, 26, 9, 13, 140, 141, 39, 45, 48, 528, 78, 135, 61, 94, 88, 90, 128, 262, 253, 203, 144, 40, 79, 136, 62, 207, 667, 210, 113, 169, 98, 119, 106, 188, 200, 235, 197, 239, 255, 258, 281, 283, 292, 299, 71, 41, 79, 63, 138, 129, 162, 164, 172, 234, 233, 244, 305, 312, 333, 319, 323, 342);
        Collections.reverse(prioridad);
        Comparator<LigaDTO> ligaComparator = new Comparator<LigaDTO>() {
            @Override
            public int compare(LigaDTO liga1, LigaDTO liga2) {
                Integer index1 = prioridad.indexOf(liga1.getId());
                Integer index2 = prioridad.indexOf(liga2.getId());
                return Integer.compare(index2, index1);
            }
        };

        Collections.sort(listaLigas, ligaComparator);

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreLiga;
        private RecyclerView recyclerViewPartidos;
        private ImageView imagenLogoLiga;
        private ImageView imagenBanderaLiga;
        private ShimmerFrameLayout shimmer_view_container;
        private LinearLayout layout;
        private LinearLayout layoutLiga;
        Handler handler;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreLiga = itemView.findViewById(R.id.textViewNombreLiga);
            recyclerViewPartidos = itemView.findViewById(R.id.recyclerViewPartidos);
            imagenLogoLiga = itemView.findViewById(R.id.imagenLogoLiga);
            imagenBanderaLiga = itemView.findViewById(R.id.imagenBanderaLiga);
            shimmer_view_container = itemView.findViewById(R.id.shimmer_view_menu);
            layout = itemView.findViewById(R.id.layoutLigaMenu);
            layoutLiga = itemView.findViewById(R.id.layoutLigaMenuPrincipal);
        }

        public void setNombreLiga(String nombre) {
            nombreLiga.setText(nombre);
        }

        public void setImagenLiga(String imagen) {
            Picasso.with(imagenLogoLiga.getContext()).load(imagen).into(imagenLogoLiga);
        }

        @SuppressLint("StaticFieldLeak")
        public void setImagenBanderaLigaLiga(String imagen) {
            if (imagen != null) {
                new AsyncTask<String, Void, Drawable>() {
                    @Override
                    protected Drawable doInBackground(String... params) {
                        try {
                            InputStream inputStream = new URL(params[0]).openStream();
                            SVG svg = SVG.getFromInputStream(inputStream);
                            return new PictureDrawable(svg.renderToPicture());
                        } catch (IOException | SVGParseException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Drawable drawable) {
                        if (drawable != null) {
                            imagenBanderaLiga.setImageDrawable(drawable);
                        }
                    }
                }.execute(imagen);
            }
        }
    }
}