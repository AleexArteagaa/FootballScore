package com.example.footballscore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.adaptador.AdaptadorListaCompeticionesPorPais;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetallesPaises extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageViewPais;
    TextView textViewPais;
    AdaptadorListaCompeticionesPorPais adaptador;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detalles_paises);

        recyclerView = findViewById(R.id.recyclerViewCompeticionesPorPais);
        imageViewPais = findViewById(R.id.imageViewBanderaExplorarDetalles);
        textViewPais = findViewById(R.id.textViewNombrePaisDetalles);

        Intent intent = getIntent();

        String nombrePais = intent.getStringExtra("pais");
        String imagen = intent.getStringExtra("bandera");

        Traductor traductor = new Traductor();
        String paisTraducido = traductor.traducir(nombrePais);

        textViewPais.setText(paisTraducido);
        setImagenBandera(imagen);

        adaptador = new AdaptadorListaCompeticionesPorPais(DetallesPaises.this);
        adaptador.showShimmer(true);
        recyclerView.setAdapter(adaptador);

        recyclerView.setLayoutManager(new LinearLayoutManager(DetallesPaises.this));

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<LeagueResponse> call = apiService.getCompeticionesPorPais(2022, nombrePais);

        call.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> leagues = response.body().getResponse();

                    adaptador.agregarListaPaises(leagues);
                    adaptador.showShimmer(false);

                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    @SuppressLint("StaticFieldLeak")
    private void setImagenBandera(String imagen) {
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
                        imageViewPais.setImageDrawable(drawable);
                    }
                }
            }.execute(imagen);
        }
    }
}
