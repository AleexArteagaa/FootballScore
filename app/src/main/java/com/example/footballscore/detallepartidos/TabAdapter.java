package com.example.footballscore.detallepartidos;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.footballscore.detallepartidos.alineaciones.AlineacionFragment;
import com.example.footballscore.detallepartidos.eventos.EventosFragment;
import com.example.footballscore.entidades.PartidoDTO;

public class TabAdapter extends FragmentPagerAdapter {

    private PartidoDTO partidoDTO;
    private boolean isNotStarted;
    public TabAdapter(@NonNull FragmentManager fm, PartidoDTO partidoDTO,boolean isNotStarted ) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.partidoDTO= partidoDTO;
        this.isNotStarted=isNotStarted;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AlineacionFragment(partidoDTO);
            case 1:
                return new EventosFragment(partidoDTO);
            case 2:
                if (!isNotStarted) {
                    return new EstadisticasFragment(partidoDTO);
                } else {
                    return new ApuestasFragment(partidoDTO);
                }
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Alineación";
            case 1:
                return "Eventos";
            case 2:
                if (!isNotStarted) {
                    return "Estadisticas";
                } else {
                    return "Apuestas";
                }
            default:
                return "";
        }
    }
}
