package com.example.footballscore.detallepartidos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.footballscore.DetalleEquipos;
import com.example.footballscore.R;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.entidades.equipos.TeamResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TabbedActivityDetallePartido extends AppCompatActivity {

    private boolean isNotStarted;
    PartidoDTO partido;
    AppBarLayout appBarLayout;
    String codeLocalTeam;
    String codeAwayTeam;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_partidos);


        TextView textViewEquipoLocal = findViewById(R.id.textViewHomeTeamDetallePartidos);
        TextView textViewEquipoVisitante = findViewById(R.id.textViewAwayTeamDetallePartidos);
        TextView textViewResultado = findViewById(R.id.textViewResultadoDetallesDetallePartidos);
        TextView textViewJornada = findViewById(R.id.textViewRoundDetallePartidos);
        ImageView imagenLocal = findViewById(R.id.imageViewHomeTeamDetallePartidos);
        ImageView imagenVisitante = findViewById(R.id.imageViewAwayTeamDetallePartidos);
        appBarLayout = findViewById(R.id.appBarLayoutDetallePartido);

        textViewEquipoLocal.setTextColor(Color.WHITE);
        textViewEquipoVisitante.setTextColor(Color.WHITE);
        textViewResultado.setTextColor(Color.WHITE);
        textViewJornada.setTextColor(Color.WHITE);

        Bundle bundle = getIntent().getExtras();

        partido = (PartidoDTO) bundle.getSerializable("partido");
        partidoDetalleCabecera(textViewEquipoLocal, textViewEquipoVisitante, textViewResultado, textViewJornada, imagenLocal, imagenVisitante, partido);

        isNotStarted = bundle.getBoolean("isNotStarted", true);

        ViewPager viewPager = findViewById(R.id.viewPagerDetalle);
        TabAdapter tabAdapter;


        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);


        Call<TeamResponse> call = apiService.getEquipoPorId(partido.getIdHomeTeam());
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> equipo = response.body().getResponse();

                    codeLocalTeam = equipo.get(0).getTeam().getCode();

                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Call<TeamResponse> call2 = apiService.getEquipoPorId(partido.getIdAwayTeam());
        call2.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> equipo = response.body().getResponse();

                    codeAwayTeam = equipo.get(0).getTeam().getCode();

                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        if (isNotStarted==true) {
            tabAdapter = new TabAdapter(getSupportFragmentManager(), partido, isNotStarted);
        } else {
            tabAdapter = new TabAdapter(getSupportFragmentManager(), partido, false);
            TabLayout tabLayout = findViewById(R.id.tabDetallePartidos);
            tabLayout.addTab(tabLayout.newTab().setText("Estadísticas"));
            tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
            tabLayout.setupWithViewPager(viewPager);
        }

        viewPager.setAdapter(tabAdapter);

        TabLayout tabLayout = findViewById(R.id.tabDetallePartidos);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        imagenLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), DetalleEquipos.class);
                intent.putExtra("equipo", partido.getIdHomeTeam());
                view.getContext().startActivity(intent);

            }
        });
        imagenVisitante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), DetalleEquipos.class);
                intent.putExtra("equipo", partido.getIdAwayTeam());
                view.getContext().startActivity(intent);

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
                        textViewEquipoLocal.setText(codeLocalTeam);
                        textViewEquipoVisitante.setText(codeAwayTeam);

                        isCollapsed = true;
                    }
                } else {
                    // No completamente colapsado
                    if (isCollapsed) {
                        textViewEquipoLocal.setText(partido.getHomeTeam());
                        textViewEquipoVisitante.setText(partido.getAwayTeam());
                        isCollapsed = false;
                    }
                }
            }
        });



    }

    private static void partidoDetalleCabecera(TextView textViewEquipoLocal, TextView textViewEquipoVisitante, TextView textViewResultado, TextView textViewJornada, ImageView imagenLocal, ImageView imagenVisitante, PartidoDTO partido) {
        if (partido.getEstado().equals("Not Started")) {
            textViewResultado.setText(partido.getDate().substring(11, 16));
            textViewEquipoLocal.setText(partido.getHomeTeam());
            textViewEquipoVisitante.setText(partido.getAwayTeam());
            String numero;
            if (partido.getRound().startsWith("Regular Season")) {
                numero = "Jornada " + partido.getRound().substring(16);
            } else {
                numero = partido.getRound();
            }
            textViewJornada.setText(numero);
            Picasso.with(imagenLocal.getContext()).load(partido.getHomeLogo()).into(imagenLocal);
            Picasso.with(imagenVisitante.getContext()).load(partido.getAwayLogo()).into(imagenVisitante);

        } else if (partido.getEstado().equals("Match Abandoned")) {
            textViewResultado.setText("APL");
            textViewEquipoLocal.setText(partido.getHomeTeam());
            textViewEquipoVisitante.setText(partido.getAwayTeam());
            String numero;
            if (partido.getRound().startsWith("Regular Season")) {
                numero = "Jornada " + partido.getRound().substring(16);
            } else {
                numero = partido.getRound();
            }
            textViewJornada.setText(numero);
            Picasso.with(imagenLocal.getContext()).load(partido.getHomeLogo()).into(imagenLocal);
            Picasso.with(imagenVisitante.getContext()).load(partido.getAwayLogo()).into(imagenVisitante);

        } else {
            textViewResultado.setText(partido.getHomeScore() + " : " + partido.getAwayScore());
            textViewEquipoLocal.setText(partido.getHomeTeam());
            textViewEquipoVisitante.setText(partido.getAwayTeam());
            String numero;
            if (partido.getRound().startsWith("Regular Season")) {
                numero = "Jornada " + partido.getRound().substring(16);
            } else {
                numero = partido.getRound();
            }
            textViewJornada.setText(numero);
            Picasso.with(imagenLocal.getContext()).load(partido.getHomeLogo()).into(imagenLocal);
            Picasso.with(imagenVisitante.getContext()).load(partido.getAwayLogo()).into(imagenVisitante);
        }
    }
}

