package com.example.footballscore.detallepartidos.alineaciones;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.footballscore.R;
import com.example.footballscore.entidades.partidos.alineaciones.Substitutes;

import java.util.ArrayList;
import java.util.List;

public class AlineacionLocalFragment extends Fragment {
    ArrayList<Substitutes> suplentes;

    public AlineacionLocalFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_alineacion_local, container, false);


        /*RecyclerView recyclerView = view.findViewById(R.id.recyclerSuplentesLocal);
        SubstitutesAdapter adapterSubs = new SubstitutesAdapter(getContext(), suplentes);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapterSubs);
        recyclerView.setLayoutManager(layoutManager);
        adapterSubs.notifyDataSetChanged();*/

        return view;
    }
}