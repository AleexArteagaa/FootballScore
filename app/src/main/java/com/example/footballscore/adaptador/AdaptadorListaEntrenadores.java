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
import com.example.footballscore.DetalleEntrenadores;
import com.example.footballscore.R;
import com.example.footballscore.entidades.EntrenadorDTO;
import com.example.footballscore.entidades.PlantillaDTO;
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
import retrofit2.Response;
import retrofit2.Retrofit;

public class AdaptadorListaEntrenadores extends RecyclerView.Adapter<AdaptadorListaEntrenadores.ViewHolder>{

    private ArrayList<EntrenadorDTO> entrenadores;
    private Context contexto;

    private String enlaceImagenBandera;


    public AdaptadorListaEntrenadores(Context context, ArrayList<EntrenadorDTO> entrenadores){
        this.contexto = context;
        this.entrenadores = entrenadores;

    }


    @NonNull
    @Override
    public AdaptadorListaEntrenadores.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View posicionesLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_entrenador, parent, false);
        return new AdaptadorListaEntrenadores.ViewHolder(posicionesLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListaEntrenadores.ViewHolder holder, int position) {

        EntrenadorDTO entrenador = entrenadores.get(position);

        int id = entrenador.getId();

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetalleEntrenadores.class);
                intent.putExtra("entrenador", id);
                v.getContext().startActivity(intent);
            }
        });



        String nombreEntrenador = entrenador.getNombre();
        System.out.println(nombreEntrenador);
        String fotoEntrenador = entrenador.getFoto();
        String banderaEntrenador = entrenador.getBandera();


        holder.setNombre(nombreEntrenador);
        holder.setImagenEntrenador(fotoEntrenador);

        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<PaisesResponse> call2 = apiService.getPaisPorNombre(banderaEntrenador);
        call2.enqueue(new Callback<PaisesResponse>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<PaisesResponse> call, Response<PaisesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> pais = response.body().getResponse();

                    enlaceImagenBandera = pais.get(0).getFlag();

                    if (enlaceImagenBandera != null) {
                        new AsyncTask<String, Void, Drawable>() {
                            @Override
                            protected Drawable doInBackground(String... params) {
                                try {
                                    InputStream inputStream = new URL(params[0]).openStream();
                                    SVG svg = SVG.getFromInputStream(inputStream);
                                    return new PictureDrawable(svg.renderToPicture());
                                } catch (IOException |
                                         SVGParseException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }

                            @Override
                            protected void onPostExecute(Drawable drawable) {
                                if (drawable != null) {
                                    holder.imagenPaisEntrenador.setImageDrawable(drawable);
                                }
                            }
                        }.execute(enlaceImagenBandera);
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
        return entrenadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewnombreEntrenador;
        private ImageView imagenEntrenador;
        private ImageView imagenPaisEntrenador;
        private LinearLayout layout;


        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewnombreEntrenador = itemView.findViewById(R.id.textViewNombreEntrenadorDetalleEquipo);
            imagenEntrenador = itemView.findViewById(R.id.imageViewEntrenadorDetalleEquipo);
            recyclerView = itemView.findViewById(R.id.recyclerViewPosicionesPorEquipo);
            imagenPaisEntrenador = itemView.findViewById(R.id.imageViewPaisEntrenadorDetalleEquipo);
            layout = itemView.findViewById(R.id.layoutEntrenador);
        }

        public void setNombre(String nombre) {
            textViewnombreEntrenador.setText(nombre);
        }

        public void setImagenEntrenador(String imagen) {
            Picasso.with(imagenEntrenador.getContext()).load(imagen).into(imagenEntrenador);
        }

        @SuppressLint("StaticFieldLeak")
        public void setImagenBanderaEntrenador(String imagen) {
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
                            imagenPaisEntrenador.setImageDrawable(drawable);
                        }
                    }
                }.execute(imagen);
            }

        }

    }
}
