package com.example.footballscore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.footballscore.LoginActivity;
import com.example.footballscore.MenuPrincipal;
import com.example.footballscore.R;

public class NosotrosFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_nosotros, container, false);

        // Obtener referencias a los elementos del layout
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView contentTextView = view.findViewById(R.id.contentTextView);
        ImageView backButton = view.findViewById(R.id.backButton);

        // Configurar el evento click del botón de retorno
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar de regreso al menú principal
                Intent menuIntent = new Intent(getActivity(), MenuPrincipal.class);
                startActivity(menuIntent);

            }
        });

        // Configurar el contenido de los elementos
        titleTextView.setText("Sobre Nosotros");
        contentTextView.setText("Somos una aplicación dedicada a proporcionar información y resultados de fútbol en tiempo real. Nuestro objetivo es mantener a los aficionados al fútbol actualizados con los últimos eventos y noticias del mundo del fútbol.");

        return view;
    }
}

