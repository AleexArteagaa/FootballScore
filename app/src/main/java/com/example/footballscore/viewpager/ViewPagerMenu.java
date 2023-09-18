package com.example.footballscore.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footballscore.fragments.partidos.menu.AyerFragment;
import com.example.footballscore.fragments.partidos.menu.HoyFragment;
import com.example.footballscore.fragments.partidos.menu.MananaFragment;

public class ViewPagerMenu extends FragmentStateAdapter {


    public ViewPagerMenu(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AyerFragment();
            case 1:
                return new HoyFragment();
            case 2:
                return new MananaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


}
