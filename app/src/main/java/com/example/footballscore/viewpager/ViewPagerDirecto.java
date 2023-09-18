package com.example.footballscore.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footballscore.fragments.partidos.directo.DirectoFragment;
import com.example.footballscore.repositorioapi.APIClient;

import retrofit2.Retrofit;

public class ViewPagerDirecto extends FragmentStateAdapter {

    private Retrofit retrofit;

    public ViewPagerDirecto(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.retrofit = APIClient.getRetrofitInstance();

        }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DirectoFragment(retrofit);

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
