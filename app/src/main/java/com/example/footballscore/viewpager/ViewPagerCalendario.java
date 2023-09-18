package com.example.footballscore.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.footballscore.fragments.partidos.calendario.AnteriorFragment;
import com.example.footballscore.fragments.partidos.calendario.SeleccionadoFragment;
import com.example.footballscore.fragments.partidos.calendario.SiguienteFragment;
import com.example.footballscore.repositorioapi.APIClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Retrofit;

public class ViewPagerCalendario extends FragmentStateAdapter {

    private String fecha;
    private String fechaAnterior;
    private String fechaSiguiente;
    private Retrofit retrofit;

    public ViewPagerCalendario(@NonNull FragmentActivity fragmentActivity, String fecha) {
        super(fragmentActivity);
        this.retrofit = APIClient.getRetrofitInstance();


        if (fecha != null){
            this.fecha = fecha;
            // Obtener la fecha anterior y la fecha siguiente
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(fecha);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, -1);
                fechaAnterior = sdf.format(calendar.getTime());
                calendar.add(Calendar.DATE, 2);
                fechaSiguiente = sdf.format(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AnteriorFragment(fechaAnterior, retrofit);
            case 1:
                return new SeleccionadoFragment(fecha, retrofit);
            case 2:
                return new SiguienteFragment(fechaSiguiente, retrofit);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
