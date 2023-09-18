package com.example.footballscore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.entidades.JugadorDTO;
import com.example.footballscore.entidades.PlantillaDTO;
import com.example.footballscore.entidades.PosicionDTO;
import com.example.footballscore.entidades.entrenadores.EntrenadoresResponse;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.fragments.entrenadores.InformacionEntrenadorFragment;
import com.example.footballscore.fragments.entrenadores.TrayectoriaEntrenadorFragment;
import com.example.footballscore.fragments.entrenadores.TrofeosEntrenadorFragment;
import com.example.footballscore.fragments.jugadores.InformacionJugadorFragment;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.example.footballscore.viewpager.ViewPagerDetalleEntrenador;
import com.example.footballscore.viewpager.ViewPagerDetalleJugador;
import com.google.android.material.tabs.TabLayout;
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

public class DetalleEntrenadores  extends AppCompatActivity {

    ImageView imagenEquipo;
    ImageView imagenEntrenador;
    ImageView imagenBandera;
    TextView textViewNombreEntrenador;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerDetalleEntrenador viewPagerDetalleEntrenador;
    String enlaceImagenBandera;
    int idEntrenador;


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detalle_entrenadores);

        imagenEquipo = findViewById(R.id.imageViewEquipoDetalleEntrenador);
        imagenEntrenador = findViewById(R.id.imageViewEntrenadorDetalleEntrenador);
        imagenBandera = findViewById(R.id.imageViewBanderaDetalleEntrenador);
        textViewNombreEntrenador = findViewById(R.id.textViewNombreEntrenadorDetallesEntrenador);
        tabLayout = findViewById(R.id.tabsEntrenadores);
        viewPager = findViewById(R.id.view_pager_entrenadores);

        Intent intent = getIntent();

        idEntrenador = intent.getIntExtra("entrenador", 0);


        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<EntrenadoresResponse> callEntrenador = apiService.getEntrenadorPorId(idEntrenador);
        callEntrenador.enqueue(new Callback<EntrenadoresResponse>() {
            @Override
            public void onResponse(Call<EntrenadoresResponse> call, Response<EntrenadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> entrenador = response.body().getResponse();

                    String nombreEntrenador = entrenador.get(0).getName();
                    String fotoEntrenador = entrenador.get(0).getPhoto();
                    String fotoEquipo = entrenador.get(0).getTeam().getLogo();
                    String paisEntrenador = entrenador.get(0).getNationality();

                    textViewNombreEntrenador.setText(nombreEntrenador);
                    Picasso.with(imagenEntrenador.getContext()).load(fotoEntrenador).into(imagenEntrenador);
                    Picasso.with(imagenEquipo.getContext()).load(fotoEquipo).into(imagenEquipo);

                    Call<PaisesResponse> call2 = apiService.getPaisPorNombre(paisEntrenador);
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
                                                imagenBandera.setImageDrawable(drawable);
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
            }

            @Override
            public void onFailure(Call<EntrenadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        tabLayout.setupWithViewPager(viewPager);
        viewPagerDetalleEntrenador = new ViewPagerDetalleEntrenador(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerDetalleEntrenador.addFragment(new InformacionEntrenadorFragment(idEntrenador), "Información");
        viewPagerDetalleEntrenador.addFragment(new TrayectoriaEntrenadorFragment(idEntrenador), "Trayectoria");
        viewPagerDetalleEntrenador.addFragment(new TrofeosEntrenadorFragment(idEntrenador), "Trofeos");


        viewPager.setAdapter(viewPagerDetalleEntrenador);

        viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).select();
    }


    }
