package com.example.footballscore.fragments;




import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;



import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

;
import com.example.footballscore.entidades.LigaDTO;
import com.example.footballscore.entidades.PartidoDTO;
import com.example.footballscore.entidades.partidos.PartidosResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.example.footballscore.viewpager.ViewPagerMenu;
import com.example.footballscore.R;
import com.example.footballscore.viewpager.ViewPagerCalendario;
import com.example.footballscore.viewpager.ViewPagerDirecto;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PartidosFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerMenu viewPagerMenu;
    private ViewPagerDirecto viewPagerDirecto;
    private ViewPagerCalendario viewPagerCalendario;
    private SwipeRefreshLayout refreshLayout;
    private Menu menu;

    private int selectedItem = -1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partidos, container, false);


        tabLayout = view.findViewById(R.id.tabs);
        viewPager2 = view.findViewById(R.id.view_pager);
        refreshLayout = view.findViewById(R.id.swiperefresh);

        refreshLayout.setColorSchemeColors(0xFF047709);

        viewPagerMenu = new ViewPagerMenu(requireActivity());
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Ayer"));
        tabLayout.addTab(tabLayout.newTab().setText("Hoy"));
        tabLayout.addTab(tabLayout.newTab().setText("Mañana"));
        viewPager2.setAdapter(viewPagerMenu);
        viewPager2.setCurrentItem(1);
        tabLayout.getTabAt(1).select();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();

            }
        });



        Toolbar toolbar = view.findViewById(R.id.toolbarConf);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int posicion = tabLayout.getSelectedTabPosition();

                viewPager2.setAdapter(viewPagerMenu);
                viewPager2.setCurrentItem(posicion);
                tabLayout.getTabAt(posicion).select();

                refreshLayout.setRefreshing(false);
            }


        });



        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.configuration_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);

        if (selectedItem == R.id.action_hoy) {
            menu.findItem(R.id.action_calendar).setVisible(false);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {


                    viewPagerDirecto= new ViewPagerDirecto(requireActivity());

                    viewPager2.setAdapter(viewPagerDirecto);
                    viewPager2.setCurrentItem(0);
                    tabLayout.getTabAt(0).select();

                    refreshLayout.setRefreshing(false);


                }
            });
        }else {
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshLayout.setRefreshing(false);
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_calendar) {



            selectedItem = R.id.action_calendar;

            Calendar calendario = Calendar.getInstance();
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.WHITE);
            drawable.setCornerRadius(50);
            DatePickerDialog.OnDateSetListener listenerFecha = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String fechaSeleccionada = String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    Calendar calendarAnterior = (Calendar) calendar.clone();
                    calendarAnterior.add(Calendar.DATE, -1);

                    Calendar calendarSiguiente = (Calendar) calendar.clone();
                    calendarSiguiente.add(Calendar.DATE, 1);

                    SimpleDateFormat formatoDiaSemana = new SimpleDateFormat("EEE dd MMM", new Locale("es"));

                    String textoAnterior = formatoDiaSemana.format(calendarAnterior.getTime());
                    String textoSeleccionado = formatoDiaSemana.format(calendar.getTime());
                    String textoSiguiente = formatoDiaSemana.format(calendarSiguiente.getTime());

                    Retrofit retrofit = APIClient.getRetrofitInstance();

                    ServicioAPI apiService = retrofit.create(ServicioAPI.class);

                    Call<PartidosResponse> call = apiService.getPartidosPorDia(fechaSeleccionada);
                    call.enqueue(new Callback<PartidosResponse>() {
                        @Override
                        public void onResponse(Call<PartidosResponse> call, Response<PartidosResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<com.example.footballscore.entidades.Response> partidos = response.body().getResponse();

                                if (partidos.size() > 0){

                                    tabLayout.removeAllTabs();

                                    viewPagerCalendario = new ViewPagerCalendario(requireActivity(), fechaSeleccionada);
                                    viewPager2.setAdapter(viewPagerCalendario);

                                    TabLayout.Tab tabAnterior = tabLayout.newTab().setText(textoAnterior);
                                    TabLayout.Tab tabSeleccionado = tabLayout.newTab().setText(textoSeleccionado);
                                    TabLayout.Tab tabSiguiente = tabLayout.newTab().setText(textoSiguiente);

                                    tabLayout.addTab(tabAnterior);
                                    tabLayout.addTab(tabSeleccionado);
                                    tabLayout.addTab(tabSiguiente);
                                    viewPager2.setAdapter(viewPagerCalendario);

                                    viewPager2.setCurrentItem(1, false);
                                    tabSeleccionado.select();


                                }else {
                                    Snackbar snackbar = Snackbar.make(viewPager2, "No hay partidos para este dia", Snackbar.LENGTH_SHORT);
                                    View snackbarView = snackbar.getView();
                                    snackbarView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#047709")));

                                    snackbar.show();
                                }


                            }
                        }

                        @Override
                        public void onFailure(Call<PartidosResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });





                }
            };

            DatePickerDialog dialogoFecha = new DatePickerDialog(requireContext(), R.style.GreenDatePickerDialogStyle, listenerFecha, año, mes, dia);
            dialogoFecha.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
            dialogoFecha.getDatePicker().setCalendarViewShown(false);
            dialogoFecha.getDatePicker().setSpinnersShown(true);

            dialogoFecha.getWindow().setBackgroundDrawable(drawable);

            dialogoFecha.show();

            return true;
        }
        if (id == R.id.action_hoy){

            Retrofit retrofit = APIClient.getRetrofitInstance();

            ServicioAPI apiService = retrofit.create(ServicioAPI.class);

            Call<PartidosResponse> call = apiService.getPartidosEnJuego();
            call.enqueue(new Callback<PartidosResponse>() {
                @Override
                public void onResponse(Call<PartidosResponse> call, Response<PartidosResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<com.example.footballscore.entidades.Response> partidos = response.body().getResponse();

                        if (partidos.size() > 0 ){
                            selectedItem = R.id.action_hoy;

                            tabLayout.removeAllTabs();

                            viewPagerDirecto= new ViewPagerDirecto(requireActivity());
                            TabLayout.Tab tabEnDirecto = tabLayout.newTab().setText("En directo");

                            tabLayout.addTab(tabEnDirecto);
                            viewPager2.setAdapter(viewPagerDirecto);
                            viewPager2.setCurrentItem(1);
                            tabEnDirecto.select();
                       }else {
                            Snackbar snackbar = Snackbar.make(viewPager2, "No hay partidos en este momento", Snackbar.LENGTH_SHORT);
                            View snackbarView = snackbar.getView();
                            snackbarView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#047709")));

                            snackbar.show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<PartidosResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });


        }

        return super.onOptionsItemSelected(item);
    }

}
