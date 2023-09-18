package com.example.footballscore.adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.R;
import com.example.footballscore.entidades.Response;
import com.example.footballscore.entidades.TrofeoDTO;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class AdaptadorTrofeosJugador extends RecyclerView.Adapter<AdaptadorTrofeosJugador.ViewHolder>{

    private Context contexto;
    private ArrayList<TrofeoDTO> datosTrofeos;
    private boolean showShimmer = true;
    private String enlaceBandera;


    public AdaptadorTrofeosJugador(Context contexto, ArrayList<TrofeoDTO> trofeos) {
        this.contexto = contexto;
        this.datosTrofeos = trofeos;

    }

    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdaptadorTrofeosJugador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View partidosLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_trofeo_jugador, parent, false);
        return new AdaptadorTrofeosJugador.ViewHolder(partidosLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTrofeosJugador.ViewHolder holder, int position) {

        TrofeoDTO trofeo = datosTrofeos.get(position);

        holder.setPuesto(trofeo.getPuesto());
        holder.setCompeticion(trofeo.getCompeticion());

        if (trofeo.getPuesto().equalsIgnoreCase("Winner")){
            holder.imagenTrofeo.setImageResource(R.drawable.trofeo);
        } else if (trofeo.getPuesto().equalsIgnoreCase("2nd Place")) {
            holder.imagenTrofeo.setImageResource(R.drawable.medalla_plata);
        }else if (trofeo.getPuesto().equalsIgnoreCase("3rd Place")) {
            holder.imagenTrofeo.setImageResource(R.drawable.medalla_bronce);
        }


        String pais = trofeo.getPais();

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<PaisesResponse> callPais = apiService.getPaisPorNombre(pais);
        callPais.enqueue(new Callback<PaisesResponse>() {
            @Override
            public void onResponse(Call<PaisesResponse> call, retrofit2.Response<PaisesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> paises = response.body().getResponse();

                    if (paises.size() > 0) {
                        enlaceBandera = paises.get(0).getFlag();
                        holder.setImagenBanderaJugador(enlaceBandera);

                    }
                }
            }

            @Override
            public void onFailure(Call<PaisesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    @Override
    public int getItemCount() {
        return datosTrofeos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewCompeticiom;
        private TextView textViewPuesto;


        private ImageView imagenTrofeo;
        private ImageView imagenBandera;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewCompeticiom = itemView.findViewById(R.id.textViewNombreCompeticionTrofeo);
            textViewPuesto = itemView.findViewById(R.id.textViewPuestoCompeticion);
            imagenTrofeo = itemView.findViewById(R.id.imageViewTrofeoJugador);
            imagenBandera = itemView.findViewById(R.id.imageViewBanderaTrofeo);

        }

        public void setCompeticion(String competicion) {
            textViewCompeticiom.setText(competicion);
        }

        public void setPuesto(String puesto) {
            textViewPuesto.setText(puesto);
        }

        @SuppressLint("StaticFieldLeak")
        public void setImagenBanderaJugador(String imagen) {
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
                            imagenBandera.setImageDrawable(drawable);
                        }
                    }
                }.execute(imagen);
            }

        }


    }
}
