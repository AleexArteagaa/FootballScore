package com.example.footballscore.adaptador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.DetalleJugadores;
import com.example.footballscore.DetallesLiga;
import com.example.footballscore.R;
import com.example.footballscore.entidades.JugadorDTO;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
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

public class AdaptadorListaJugadores extends RecyclerView.Adapter<AdaptadorListaJugadores.ViewHolder>{

    private ArrayList<JugadorDTO> datosJugadores;
    private Context contexto;
    String enlaceBanderaJugador;
    String pais;

    public AdaptadorListaJugadores(Context context, ArrayList<JugadorDTO> jugadores){
        this.contexto = context;
        datosJugadores = jugadores;

    }


    @NonNull
    @Override
    public AdaptadorListaJugadores.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_jugador, parent, false);
        return new AdaptadorListaJugadores.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaJugadores.ViewHolder holder, int position) {

        int idJugador = datosJugadores.get(position).getId();
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetalleJugadores.class);
                intent.putExtra("jugador", idJugador);
                v.getContext().startActivity(intent);
            }
        });

        holder.setNombreJugador(datosJugadores.get(position).getName());
        holder.setImagenJugador(datosJugadores.get(position).getPhoto());

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<JugadoresResponse> callPais = apiService.getEstadisticasPorJugadorTemporada( datosJugadores.get(position).getId(),2022);
        callPais.enqueue(new Callback<JugadoresResponse>() {
            @Override
            public void onResponse(Call<JugadoresResponse> call, retrofit2.Response<JugadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> jugadores = response.body().getResponse();

                    if (jugadores.size() > 0) {

                        pais = jugadores.get(0).getPlayer().getNationality();
                        if (pais.equalsIgnoreCase("Türkiye")){
                            pais = "Turkey";
                        }

                        Call<PaisesResponse> callPais = apiService.getPaisPorNombre(pais);
                        callPais.enqueue(new Callback<PaisesResponse>() {
                            @Override
                            public void onResponse(Call<PaisesResponse> call, retrofit2.Response<PaisesResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {

                                    List<com.example.footballscore.entidades.Response> paises = response.body().getResponse();

                                    if (paises.size() > 0) {
                                        enlaceBanderaJugador = paises.get(0).getFlag();
                                        holder.setImagenBanderaJugador(enlaceBanderaJugador);

                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<PaisesResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    }
                }
            }
            @Override
            public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });


        holder.setImagenBanderaJugador(datosJugadores.get(position).getEnlaceBandera());


    }



    @Override
    public int getItemCount() {
        return datosJugadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewJugador;

        private ImageView imagenJugador;
        private ImageView imagenPaisJugador;
        private LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewJugador = itemView.findViewById(R.id.textViewNombreJugadorDetalleEquipo);
            imagenJugador = itemView.findViewById(R.id.imageViewJugadorFichaje);
            imagenPaisJugador = itemView.findViewById(R.id.imageViewPaisJugadorDetalleEquipo);
            layout = itemView.findViewById(R.id.layoutJugador);
        }

        public void setNombreJugador(String nombreJugador) {
            textViewJugador.setText(nombreJugador);
        }

        public void setImagenJugador(String imagen) {
            Picasso.with(imagenJugador.getContext()).load(imagen).into(imagenJugador);
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
                            imagenPaisJugador.setImageDrawable(drawable);
                        }
                    }
                }.execute(imagen);
            }

        }
    }
}
