package com.example.footballscore.fragments.equipos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.footballscore.R;
import com.example.footballscore.Traductor;
import com.example.footballscore.entidades.competiciones.LeagueResponse;
import com.example.footballscore.entidades.equipos.TeamResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InformacionEquipoFragment extends Fragment {


    private int idEquipo;
    private String liga;
    private TextView nombreEquipo;
    private TextView fundacionEquipo;
    private TextView paisEquipo;
    private TextView ciudadEquipo;
    private TextView ligaEquipo;
    private ImageView imagenEstadio;
    private TextView nombreEstadioEquipo;
    private TextView direccionEstadioEquipo;
    private TextView capacidadEstadioEquipo;
    private LinearLayout layoutEstadio;
    private LinearLayout layoutNombreEstadio;
    private LinearLayout layoutDireccionEstadio;
    private LinearLayout layoutCapacidadEstadio;
    private LinearLayout layoutFundacionEquipo;
    private LinearLayout layoutPaisEquipo;
    private LinearLayout layoutCiudadEquipo;
    private LinearLayout layoutLigaEquipo;

    public InformacionEquipoFragment(int idEquipo, String liga){
        this.idEquipo = idEquipo;
        this.liga = liga;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacion_equipo, container, false);

        nombreEquipo = view.findViewById(R.id.textViewNombreEquipoInformacion);
        fundacionEquipo = view.findViewById(R.id.textViewAnioFundacion);
        paisEquipo = view.findViewById(R.id.textViewPais);
        ciudadEquipo = view.findViewById(R.id.textViewCiudad);
        ligaEquipo = view.findViewById(R.id.textViewLiga);
        imagenEstadio = view.findViewById(R.id.imageViewEstadio);
        nombreEstadioEquipo = view.findViewById(R.id.textViewNombreEstadio);
        direccionEstadioEquipo = view.findViewById(R.id.textViewDireccionEstadio);
        capacidadEstadioEquipo = view.findViewById(R.id.textViewCapacidadEstadio);
        layoutEstadio = view.findViewById(R.id.layoutEstadio);
        layoutNombreEstadio = view.findViewById(R.id.layoutNombreEstadio);
        layoutCapacidadEstadio = view.findViewById(R.id.layoutCapacidadEstadio);
        layoutDireccionEstadio = view.findViewById(R.id.layoutDireccionEstadio);
        layoutFundacionEquipo = view.findViewById(R.id.layoutAnioFundacionEquipo);
        layoutPaisEquipo = view.findViewById(R.id.layoutPaisEquipo);
        layoutCiudadEquipo = view.findViewById(R.id.layoutCiudadEquipo);
        layoutLigaEquipo = view.findViewById(R.id.layoutLigaEquipo);



        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<TeamResponse> call = apiService.getEquipoPorId(idEquipo);
        call.enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> teams = response.body().getResponse();

                    if (teams.get(0).getTeam().getCode()!= null){
                        nombreEquipo.setText(teams.get(0).getTeam().getName() + " (" + teams.get(0).getTeam().getCode() + ")");
                    }else {
                        nombreEquipo.setText(teams.get(0).getTeam().getName());
                    }

                    if (teams.get(0).getTeam().getFounded() != 0){
                        fundacionEquipo.setText(teams.get(0).getTeam().getFounded() + "");
                    }else {
                        layoutFundacionEquipo.setVisibility(View.GONE);
                    }
                    if (teams.get(0).getTeam().getCountry() != null){
                        String paisIngles = teams.get(0).getTeam().getCountry();
                        Traductor traductor = new Traductor();
                        String paisTraducido = traductor.traducir(paisIngles);
                        paisEquipo.setText(paisTraducido);
                    }else {
                        layoutPaisEquipo.setVisibility(View.GONE);
                    }
                    if (teams.get(0).getVenue().getCity()!= null){
                        ciudadEquipo.setText(teams.get(0).getVenue().getCity());
                    }else {
                        layoutCiudadEquipo.setVisibility(View.GONE);
                    }
                    ligaEquipo.setText(liga);
                    Picasso.with(imagenEstadio.getContext()).load(teams.get(0).getVenue().getImage()).into(imagenEstadio);

                    if (teams.get(0).getVenue().getImage() != null && teams.get(0).getVenue().getName() != null && teams.get(0).getVenue().getAddress() != null && teams.get(0).getVenue().getCapacity() != 0) {


                        if (teams.get(0).getVenue().getName() != null) {
                            nombreEstadioEquipo.setText(teams.get(0).getVenue().getName());
                        } else {
                            layoutNombreEstadio.setVisibility(View.GONE);
                        }
                        if (teams.get(0).getVenue().getAddress() != null) {
                            direccionEstadioEquipo.setText(teams.get(0).getVenue().getAddress());
                        } else {
                            layoutDireccionEstadio.setVisibility(View.GONE);

                        }
                        if (teams.get(0).getVenue().getCapacity() != 0) {
                            capacidadEstadioEquipo.setText(teams.get(0).getVenue().getCapacity() + "");
                        } else {
                            layoutCapacidadEstadio.setVisibility(View.GONE);
                        }
                    }else {
                        layoutEstadio.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Call<LeagueResponse> call2 = apiService.getLigaPorEquipo(idEquipo);
        call2.enqueue(new Callback<LeagueResponse>() {
            @Override
            public void onResponse(Call<LeagueResponse> call, Response<LeagueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> liga = response.body().getResponse();

                    if (liga.size() > 0){
                        ligaEquipo.setText(liga.get(0).getLeague().getName());

                    }

                }
            }

            @Override
            public void onFailure(Call<LeagueResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });



        return view;
    }



}
