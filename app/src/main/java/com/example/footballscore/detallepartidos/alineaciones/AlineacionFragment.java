package com.example.footballscore.detallepartidos.alineaciones;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.footballscore.R;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.entidades.partidos.alineaciones.Substitutes;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.example.footballscore.util.MetodosAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;


public class AlineacionFragment extends Fragment {
    private PartidoDTO partidoDTO;
    public AlineacionFragment( PartidoDTO partidoDTO) {
        this.partidoDTO=partidoDTO;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alineacion, container, false);

        Retrofit retrofit = APIClient.getRetrofitInstance();

        ServicioAPI apiService = retrofit.create(ServicioAPI.class);
        MetodosAPI metodosAPI = new MetodosAPI();


        Button btnLocal = view.findViewById(R.id.alineacionLocalbtn);
        Button btnVisit = view.findViewById(R.id.alineacionVisbtn);

        btnLocal.setText(partidoDTO.getHomeTeam());
        btnVisit.setText(partidoDTO.getAwayTeam());

        btnLocal.setSelected(true);
        btnVisit.setBackgroundColor(Color.WHITE);
        btnVisit.setSelected(false);
        btnLocal.setBackgroundColor(Color.GREEN);

        ViewPager2 viewPager = view.findViewById(R.id.pagerAlineacion);
        AlineacionPagerAdapter adapter = new AlineacionPagerAdapter(AlineacionFragment.this);
        viewPager.setAdapter(adapter);

        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnLocal.isSelected()) {
                    btnLocal.setSelected(true);
                    btnVisit.setBackgroundColor(Color.WHITE);
                    btnVisit.setSelected(false);
                    btnLocal.setBackgroundColor(Color.GREEN);

                    viewPager.setCurrentItem(0);
                }
            }
        });

        btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnVisit.isSelected()) {
                    btnLocal.setSelected(false);
                    btnVisit.setBackgroundColor(Color.GREEN);
                    btnVisit.setSelected(true);
                    btnLocal.setBackgroundColor(Color.WHITE);

                    viewPager.setCurrentItem(1);
                }
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // Actualizar el estado de los botones según la página seleccionada
                if (position == 0) {
                    btnLocal.setSelected(true);
                    btnVisit.setSelected(false);
                    btnLocal.setBackgroundColor(Color.GREEN);
                    btnVisit.setBackgroundColor(Color.WHITE);

                    metodosAPI.getAlineacionesPorPartido(apiService, partidoDTO, AlineacionFragment.this, position);
                } else if (position == 1) {
                    btnLocal.setSelected(false);
                    btnVisit.setSelected(true);
                    btnLocal.setBackgroundColor(Color.WHITE);
                    btnVisit.setBackgroundColor(Color.GREEN);
                    metodosAPI.getAlineacionesPorPartido(apiService, partidoDTO, AlineacionFragment.this, position);

                }
            }
        });


        return view;
    }


}