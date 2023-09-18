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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.equipos.TeamResponse;
import com.example.footballscore.fragments.equipos.CompeticionesFragment;
import com.example.footballscore.fragments.equipos.FichajesFragment;
import com.example.footballscore.fragments.equipos.InformacionEquipoFragment;
import com.example.footballscore.fragments.equipos.PartidosDetalleEquiposFragment;
import com.example.footballscore.fragments.equipos.PlantillaFragment;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.example.footballscore.viewpager.ViewPagerDetalleEquipo;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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

public class DetalleEquipos extends AppCompatActivity {

    ImageView imagenEquipo;
    ImageView imagenLiga;
    ImageView imagenBandera;
    TextView textViewNombreEquipo;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerDetalleEquipo viewPagerDetalleEquipo;
    String nombreEquipo;
    String nombreEquipoCorto;
    String enlaceImagenEquipo;
    String enlaceImagenLiga;
    String enlaceImagenBandera;
    String ligaEquipo;

    int idEquipo;



    @SuppressLint("StaticFieldLeak")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detalle_equipos);
        imagenEquipo = findViewById(R.id.imageViewEquipoDetalleEquipo);
        imagenLiga = findViewById(R.id.imageViewLigaDetalleEquipo);
        imagenBandera = findViewById(R.id.imageViewBanderaDetalleEquipos);
        textViewNombreEquipo = findViewById(R.id.textViewNombreEquipoDetallesEquipos);
        tabLayout = findViewById(R.id.tabsEquipos);
        viewPager = findViewById(R.id.view_pager_equipos);


        Intent intent = getIntent();

        idEquipo = intent.getIntExtra("equipo", 0);

        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<TeamResponse> call = apiService.getEquipoPorId(idEquipo);
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();

                    enlaceImagenEquipo = teams.get(0).getTeam().getLogo();
                    textViewNombreEquipo.setText(teams.get(0).getTeam().getName());
                    nombreEquipo = teams.get(0).getTeam().getName();
                    nombreEquipoCorto = teams.get(0).getTeam().getCode();

                    Picasso.with(imagenEquipo.getContext()).load(enlaceImagenEquipo).into(imagenEquipo);

                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Call<LeagueResponse> callLiga = apiService.getLigaPorEquipo(idEquipo);
        callLiga.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<com.example.footballscore.entidades.Response> leagues = response.body().getResponse();

                    if (leagues.size() > 0) {


                        enlaceImagenLiga = leagues.get(0).getLeague().getLogo();
                        enlaceImagenBandera = leagues.get(0).getCountry().getFlag();
                        ligaEquipo = leagues.get(0).getLeague().getName();

                        Picasso.with(imagenLiga.getContext()).load(enlaceImagenLiga).into(imagenLiga);

                        if (enlaceImagenBandera != null) {
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
                            }.execute(enlaceImagenBandera);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        //Picasso.with(imagenEquipo.getContext()).load(enlaceImagenEquipo).into(imagenEquipo);
        //Picasso.with(imagenLiga.getContext()).load(enlaceImagenLiga).into(imagenLiga);


        tabLayout.setupWithViewPager(viewPager);
        viewPagerDetalleEquipo = new ViewPagerDetalleEquipo(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerDetalleEquipo.addFragment(new InformacionEquipoFragment(idEquipo, ligaEquipo), "Información");
        viewPagerDetalleEquipo.addFragment(new PlantillaFragment(idEquipo), "Plantilla");
        viewPagerDetalleEquipo.addFragment(new CompeticionesFragment(idEquipo), "Competiciones");
        viewPagerDetalleEquipo.addFragment(new FichajesFragment(idEquipo), "Fichajes");
        viewPagerDetalleEquipo.addFragment(new PartidosDetalleEquiposFragment(idEquipo), "Partidos");


        viewPager.setAdapter(viewPagerDetalleEquipo);


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


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isCollapsed = false;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                if (verticalOffset + totalScrollRange == 0) {
                    // Completamente colapsado
                    if (!isCollapsed) {
                        textViewNombreEquipo.setText(nombreEquipo);
                        isCollapsed = true;
                    }
                } else {
                    // No completamente colapsado
                    if (isCollapsed) {
                        textViewNombreEquipo.setText(nombreEquipo);
                        isCollapsed = false;
                    }
                }
            }
        });

        toolbar.setTitle(null);
        setSupportActionBar(toolbar);

    }
}
