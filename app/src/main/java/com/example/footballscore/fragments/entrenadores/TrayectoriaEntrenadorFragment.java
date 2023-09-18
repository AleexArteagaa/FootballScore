package com.example.footballscore.fragments.entrenadores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.adaptador.AdaptadorTrayectoriaEntrenador;
import com.example.footballscore.adaptador.AdaptadorTrayectoriaJugador;
import com.example.footballscore.entidades.entrenadores.Career;
import com.example.footballscore.entidades.entrenadores.EntrenadoresResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TrayectoriaEntrenadorFragment  extends Fragment {

    private int idEntrenador;

    private RecyclerView recyclerView;
    private AdaptadorTrayectoriaEntrenador adaptador;

    public TrayectoriaEntrenadorFragment(int idEntrenador){
        this.idEntrenador = idEntrenador;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trayectoria_entrenador, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTrayectoriaEntrenador);

        adaptador = new AdaptadorTrayectoriaEntrenador(requireActivity());

        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<EntrenadoresResponse> callEntrenador = apiService.getEntrenadorPorId(idEntrenador);
        callEntrenador.enqueue(new Callback<EntrenadoresResponse>() {
            @Override
            public void onResponse(Call<EntrenadoresResponse> call, Response<EntrenadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> entrenador = response.body().getResponse();

                   List<Career> trayectoria = entrenador.get(0).getCareer();

                   adaptador.agregarListaTrayectoria(trayectoria);



                }
            }

            @Override
            public void onFailure(Call<EntrenadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return view;
    }
}
