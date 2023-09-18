package com.example.footballscore.detallepartidos.alineaciones;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AlineacionPagerAdapter extends FragmentStateAdapter {
    public AlineacionPagerAdapter(@NonNull AlineacionFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new AlineacionLocalFragment();
        } else if (position == 1) {
            return new AlineacionVisitanteFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
