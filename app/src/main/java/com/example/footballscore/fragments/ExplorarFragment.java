package com.example.footballscore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.DetallesPaises;
import com.example.footballscore.R;
import com.example.footballscore.Traductor;
import com.example.footballscore.adaptador.AdaptadorListaPaises;
import com.example.footballscore.adaptador.RecyclerItemClickListener;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExplorarFragment extends Fragment {

    private RecyclerView recyclerViewPaises;
    private AdaptadorListaPaises adaptadorListaPaises;
    private List<com.example.footballscore.entidades.Response> listaPaises;
    private List<com.example.footballscore.entidades.Response> paisesFiltrados;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorar, container, false);

        recyclerViewPaises = view.findViewById(R.id.recyclerViewExplorar);
        SearchView searchView = view.findViewById(R.id.searchViewPais);

        adaptadorListaPaises = new AdaptadorListaPaises(getContext());

        recyclerViewPaises.setAdapter(adaptadorListaPaises);

        recyclerViewPaises.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        adaptadorListaPaises.showShimmer(true);

        Call<PaisesResponse> call = apiService.getPaises();
        call.enqueue(new Callback<PaisesResponse>() {
            @Override
            public void onResponse(Call<PaisesResponse> call, Response<PaisesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> paises = response.body().getResponse();
                    listaPaises = new ArrayList<>(paises);

                    adaptadorListaPaises.agregarListaPaises(paises);
                    adaptadorListaPaises.showShimmer(false);
                }
            }

            @Override
            public void onFailure(Call<PaisesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        recyclerViewPaises.addOnItemTouchListener(new RecyclerItemClickListener(requireContext(),  recyclerViewPaises, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemOnClick(View v, int posicion) {
                Intent intent = new Intent(v.getContext(), DetallesPaises.class);

                com.example.footballscore.entidades.Response paisSeleccionado;
                if (paisesFiltrados != null && !paisesFiltrados.isEmpty()) {
                    paisSeleccionado = paisesFiltrados.get(posicion);
                } else {
                    paisSeleccionado = listaPaises.get(posicion);
                }

                String nombrePais = paisSeleccionado.getName();
                String banderaPais = paisSeleccionado.getFlag();
                System.out.println(nombrePais);
                intent.putExtra("pais", nombrePais);
                intent.putExtra("bandera", banderaPais);
                v.getContext().startActivity(intent);
            }

            @Override
            public void onLongItemClick(View v, int posicion) {

            }
        }));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarPaises(newText);
                return true;
            }
        });



        return view;
    }

    private void filtrarPaises(String texto) {
         paisesFiltrados = new ArrayList<>();
        for (com.example.footballscore.entidades.Response pais : listaPaises) {
            String nombrePais = pais.getName();
            Traductor traductor = new Traductor();
            String paisTraducido = traductor.traducir(nombrePais);
            if (paisTraducido.toLowerCase().startsWith(texto.toLowerCase())) {
                paisesFiltrados.add(pais);
            }
        }
        adaptadorListaPaises = new AdaptadorListaPaises(getContext());
        adaptadorListaPaises.agregarListaPaises(paisesFiltrados);
        recyclerViewPaises.setAdapter(adaptadorListaPaises);
        adaptadorListaPaises.showShimmer(false);

    }
}
