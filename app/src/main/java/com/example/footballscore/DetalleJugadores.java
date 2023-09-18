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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.equipos.TeamResponse;
import com.example.footballscore.entidades.jugadores.JugadoresResponse;
import com.example.footballscore.entidades.jugadores.TemporadasResponse;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.fragments.equipos.CompeticionesFragment;
import com.example.footballscore.fragments.equipos.FichajesFragment;
import com.example.footballscore.fragments.equipos.InformacionEquipoFragment;
import com.example.footballscore.fragments.equipos.PartidosDetalleEquiposFragment;
import com.example.footballscore.fragments.equipos.PlantillaFragment;
import com.example.footballscore.fragments.jugadores.EstadisticasJugadorFragment;
import com.example.footballscore.fragments.jugadores.FichajesJugadorFragment;
import com.example.footballscore.fragments.jugadores.InformacionJugadorFragment;
import com.example.footballscore.fragments.jugadores.LesionesJugadorFragment;
import com.example.footballscore.fragments.jugadores.TrayectoriaJugadorFragment;
import com.example.footballscore.fragments.jugadores.TrofeosJugadorFragment;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.example.footballscore.viewpager.ViewPagerDetalleJugador;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetalleJugadores extends AppCompatActivity {

    ImageView imagenEquipo;
    ImageView imagenJugador;
    ImageView imagenBandera;
    TextView textViewNombreJugador;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerDetalleJugador viewPagerDetalleJugador;
    int ultimaTemporada;
    String enlaceFotoJugador;
    String enlaceImagenEquipo;
    String enlaceImagenBandera;
    String nacionalidadJugador;
    int idJugador;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detalle_jugadores);

        imagenEquipo = findViewById(R.id.imageViewEquipoDetalleJugador);
        imagenJugador = findViewById(R.id.imageViewJugadorDetalleJugadores);
        imagenBandera = findViewById(R.id.imageViewBanderaDetalleJugador);
        textViewNombreJugador = findViewById(R.id.textViewNombreJugadorDetallesJugador);
        tabLayout = findViewById(R.id.tabsJugadores);
        viewPager = findViewById(R.id.view_pager_jugadores);

        Intent intent = getIntent();

        idJugador = intent.getIntExtra("jugador", 0);

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);


        Call<TemporadasResponse> callLiga = apiService.getTemporadasPorJugador(idJugador);
        callLiga.enqueue(new Callback<TemporadasResponse>() {
            @Override
            public void onResponse(Call<TemporadasResponse> call, Response<TemporadasResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Integer> temporadas = response.body().getResponse();

                    if (temporadas.size() > 0) {


                        ultimaTemporada = temporadas.get(temporadas.size() - 1);

                        Call<JugadoresResponse> call2 = apiService.getEstadisticasPorJugadorTemporada(idJugador, ultimaTemporada);
                        call2.enqueue(new Callback<JugadoresResponse>() {
                            @Override
                            public void onResponse(Call<JugadoresResponse> call, Response<JugadoresResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    List<com.example.footballscore.entidades.Response> jugador = response.body().getResponse();


                                    enlaceFotoJugador = jugador.get(0).getPlayer().getPhoto();
                                    enlaceImagenEquipo = jugador.get(0).getStatistics().get(0).getTeam().getLogo();
                                    textViewNombreJugador.setText(jugador.get(0).getPlayer().getName());
                                    nacionalidadJugador = jugador.get(0).getPlayer().getNationality();
                                    if (nacionalidadJugador.equalsIgnoreCase("Türkiye")){
                                        nacionalidadJugador = "Turkey";
                                    }

                                    Picasso.with(imagenJugador.getContext()).load(enlaceFotoJugador).into(imagenJugador);
                                    Picasso.with(imagenEquipo.getContext()).load(enlaceImagenEquipo).into(imagenEquipo);

                                    Call<PaisesResponse> call2 = apiService.getPaisPorNombre(nacionalidadJugador);
                                    call2.enqueue(new Callback<PaisesResponse>() {
                                        @SuppressLint("StaticFieldLeak")
                                        @Override
                                        public void onResponse(Call<PaisesResponse> call, Response<PaisesResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                List<com.example.footballscore.entidades.Response> pais = response.body().getResponse();

                                                if (pais.size() > 0) {

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
                                        }

                                        @Override
                                        public void onFailure(Call<PaisesResponse> call, Throwable t) {
                                            t.printStackTrace();
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onFailure(Call<JugadoresResponse> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<TemporadasResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        tabLayout.setupWithViewPager(viewPager);
        viewPagerDetalleJugador = new ViewPagerDetalleJugador(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerDetalleJugador.addFragment(new InformacionJugadorFragment(idJugador), "Información");
        viewPagerDetalleJugador.addFragment(new EstadisticasJugadorFragment(idJugador), "Estadísticas");
        viewPagerDetalleJugador.addFragment(new TrayectoriaJugadorFragment(idJugador), "Trayectoria");
        viewPagerDetalleJugador.addFragment(new FichajesJugadorFragment(idJugador), "Fichajes");
        viewPagerDetalleJugador.addFragment(new TrofeosJugadorFragment(idJugador), "Trofeos");
        viewPagerDetalleJugador.addFragment(new LesionesJugadorFragment(idJugador), "Lesiones/Sanciones");

        viewPager.setAdapter(viewPagerDetalleJugador);

        viewPager.setCurrentItem(0);
        tabLayout.getTabAt(0).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
