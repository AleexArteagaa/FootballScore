package com.example.footballscore.fragments.entrenadores;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.footballscore.R;
import com.example.footballscore.Traductor;
import com.example.footballscore.entidades.entrenadores.EntrenadoresResponse;
import com.example.footballscore.entidades.paises.PaisesResponse;
import com.example.footballscore.repositorioapi.APIClient;
import com.example.footballscore.repositorioapi.ServicioAPI;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InformacionEntrenadorFragment  extends Fragment {

    private int idEntrenador;
    private TextView textViewNombreCompleto;
    private TextView textViewNombre;
    private TextView textViewEdad;
    private TextView textViewPeso;
    private TextView textViewAltura;
    private TextView textViewFechaNacimiento;
    private TextView textViewPaisNacimiento;
    private TextView textViewCiudadNacimiento;
    private TextView textViewPosicion;
    private LinearLayout layoutPeso;
    private LinearLayout layoutAltura;
    private LinearLayout layoutEdad;
    private LinearLayout layoutFechaNacimiento;
    private LinearLayout layoutPaisNacimiento;
    private LinearLayout layoutCiudadNacimiento;
    private LinearLayout layoutsInformacion;
    private LinearLayout layoutPesoEdadAltura;

    public InformacionEntrenadorFragment(int idEntrenador){
        this.idEntrenador = idEntrenador;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacion_entrenador, container, false);

        textViewNombreCompleto = view.findViewById(R.id.textViewNombreCompletoEntrenador);
        textViewNombre = view.findViewById(R.id.textViewNombreEntrenadorInformacion);
        textViewEdad = view.findViewById(R.id.textViewEdadEntrenadorInformacion);
        textViewPeso = view.findViewById(R.id.textViewPesoEntrenadorInformacion);
        textViewAltura = view.findViewById(R.id.textViewAlturaEntrenadorInformacion);
        textViewFechaNacimiento = view.findViewById(R.id.textViewFechaNacimientoEntrenador);
        textViewPaisNacimiento = view.findViewById(R.id.textViewPaisNacimientoEntrenador);
        textViewCiudadNacimiento = view.findViewById(R.id.textViewCiudadNacimientoEntrenador);
        layoutEdad = view.findViewById(R.id.layoutEdadEntrenador);
        layoutAltura = view.findViewById(R.id.layoutAlturaEntrenador);
        layoutPeso = view.findViewById(R.id.layoutPesoEntrenador);
        layoutCiudadNacimiento = view.findViewById(R.id.layoutCiudadNacimientoEntrenador);
        layoutFechaNacimiento = view.findViewById(R.id.layoutFechaNacimientoEntrenador);
        layoutPaisNacimiento = view.findViewById(R.id.layoutPaisNacimientoEntrenador);
        layoutsInformacion = view.findViewById(R.id.layoutsInformacionEntrenador);
        layoutPesoEdadAltura = view.findViewById(R.id.layoutPesoEdadAlturaEntrenador);


        Retrofit retrofit = APIClient.getRetrofitInstance();
        ServicioAPI apiService = retrofit.create(ServicioAPI.class);

        Call<EntrenadoresResponse> callEntrenador = apiService.getEntrenadorPorId(idEntrenador);
        callEntrenador.enqueue(new Callback<EntrenadoresResponse>() {
            @Override
            public void onResponse(Call<EntrenadoresResponse> call, Response<EntrenadoresResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.footballscore.entidades.Response> entrenador = response.body().getResponse();

                    String nombreCompleto = entrenador.get(0).getFirstname() + " " + entrenador.get(0).getLastname();
                    if (nombreCompleto != null) {
                        textViewNombreCompleto.setText(nombreCompleto);
                    } else {
                        textViewNombreCompleto.setVisibility(View.GONE);
                    }
                    if (entrenador.get(0).getName() != null) {
                        textViewNombre.setText(entrenador.get(0).getName());
                    } else {
                        textViewNombre.setVisibility(View.GONE);
                    }

                    if (entrenador.get(0).getAge() != 0 && entrenador.get(0).getHeight() != null && entrenador.get(0).getWeight() != null) {

                        if (entrenador.get(0).getAge() != 0) {
                            textViewEdad.setText(entrenador.get(0).getAge() + "");
                        } else {
                            layoutEdad.setVisibility(View.GONE);
                        }
                        if (entrenador.get(0).getHeight() != null) {
                            String altura = entrenador.get(0).getHeight();
                            altura = altura.replaceAll(" cm", "");
                            textViewAltura.setText(altura);
                        } else {
                            layoutAltura.setVisibility(View.GONE);
                        }
                        if (entrenador.get(0).getWeight() != null) {
                            String peso = entrenador.get(0).getWeight();
                            peso = peso.replaceAll(" kg", "");
                            textViewPeso.setText(peso);
                        } else {
                            layoutPeso.setVisibility(View.GONE);
                        }

                    }else {
                        layoutPesoEdadAltura.setVisibility(View.GONE);
                    }
                    if (entrenador.get(0).getBirth().getDate() != null) {
                        String fechaNacimiento = entrenador.get(0).getBirth().getDate();
                        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatoSalida = new SimpleDateFormat("d MMMM yyyy", new Locale("es"));
                        Date fecha;
                        String fechaFormateada = "";
                        try {
                            fecha = formatoEntrada.parse(fechaNacimiento);
                            fechaFormateada = formatoSalida.format(fecha);

                            String mes = fechaFormateada.split(" ")[1];
                            String mesCapitalizado = mes.substring(0, 1).toUpperCase() + mes.substring(1);
                            fechaFormateada = fechaFormateada.replace(mes, mesCapitalizado);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        textViewFechaNacimiento.setText(fechaFormateada);
                    } else {
                        layoutFechaNacimiento.setVisibility(View.GONE);
                    }
                    if (entrenador.get(0).getBirth().getCountry() != null) {
                        String paisIngles = entrenador.get(0).getBirth().getCountry();
                        Traductor traductor = new Traductor();
                        String paisTraducido = traductor.traducir(paisIngles);
                        textViewPaisNacimiento.setText(paisTraducido);
                    } else {
                        layoutPaisNacimiento.setVisibility(View.GONE);
                    }
                    if (entrenador.get(0).getBirth().getPlace() != null) {
                        textViewCiudadNacimiento.setText(entrenador.get(0).getBirth().getPlace());
                    } else {
                        layoutCiudadNacimiento.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onFailure(Call<EntrenadoresResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return view;
    }
}
