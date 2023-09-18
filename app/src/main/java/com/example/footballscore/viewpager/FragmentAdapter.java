package com.example.footballscore.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footballscore.fragments.ligas.FragmentClasificacionDetalleLiga;
import com.example.footballscore.fragments.ligas.FragmentEquiposDetalleLiga;
import com.example.footballscore.fragments.ligas.FragmentEstadiosDetalleLiga;
import com.example.footballscore.fragments.ligas.FragmentPartidosDetalleLiga;

public class FragmentAdapter extends FragmentStateAdapter {

    int idLiga;
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity, int idLiga) {
        super(fragmentActivity);
        this.idLiga = idLiga;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new FragmentClasificacionDetalleLiga(idLiga);
            case 1:
                return new FragmentPartidosDetalleLiga(idLiga);
            case 2:
                return new FragmentEquiposDetalleLiga(idLiga);
            case 3:
                return new FragmentEstadiosDetalleLiga(idLiga);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
