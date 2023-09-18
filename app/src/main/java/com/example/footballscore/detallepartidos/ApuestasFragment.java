package com.example.footballscore.detallepartidos;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footballscore.R;
import com.example.footballscore.apuestas.historial.HistorialDTO;
import com.example.footballscore.entidades.PartidoDTO;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApuestasFragment extends Fragment {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    CollectionReference collectionUsers = firebaseFirestore.collection("Usuarios");
    private PartidoDTO partidoDTO;


    public ApuestasFragment(PartidoDTO partidoDTO) {
        this.partidoDTO= partidoDTO;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_apuestas, container, false);


        SharedPreferences prefs = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        RadioButton radioButtonLocal= v.findViewById(R.id.radioButtonOptionLocal);
        RadioButton radioButtonVisitante= v.findViewById(R.id.radioButtonOptionVisitante);
        SeekBar seekbar= v.findViewById(R.id.seekBarDetalles);
        TextView textCantidad= v.findViewById(R.id.textCantSelected);
        TextView saldoDispDetalle= v.findViewById(R.id.saldoApuestasDetalle);

        saldoDispDetalle.setText(String.valueOf(prefs.getInt("saldo", 0)));


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int cantidadSeleccionada = progress;
                textCantidad.setText(String.valueOf(cantidadSeleccionada));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Acciones a realizar cuando se comienza a mover la barra de progreso
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Acciones a realizar cuando se detiene de mover la barra de progreso
            }
        });

        Switch switchNotificaciones= v.findViewById(R.id.switchNotificaciones);
        Button buttonApostar= v.findViewById(R.id.buttonApostar);


        radioButtonLocal.setText(partidoDTO.getHomeTeam());
        radioButtonVisitante.setText(partidoDTO.getAwayTeam());


        buttonApostar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (radioButtonLocal.isChecked()) {
                    collectionUsers.whereEqualTo("Nombre",prefs.getString("user",null)).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            int saldoActual = Objects.requireNonNull(task.getResult().getDocuments().get(0).getLong("Saldo")).intValue();
                            int nuevoSaldo = saldoActual - seekbar.getProgress();
                            saldoDispDetalle.setText(nuevoSaldo+"");
                            String checkRBLocal=  "Victoria Local ("+partidoDTO.getHomeTeam()+" vs "+partidoDTO.getAwayTeam()+")";


                            String documentId = task.getResult().getDocuments().get(0).getId();
                            DocumentReference docRef = collectionUsers.document(documentId);
                            docRef.update("Saldo", nuevoSaldo, "Apuestas", FieldValue.arrayUnion(new HistorialDTO(partidoDTO.getEstado(), checkRBLocal, seekbar.getProgress())))

                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getContext(), "Apuesta realizada correctamente", Toast.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "No se ha podido realizar la apuesta", Toast.LENGTH_LONG).show();

                                    });
                        }
                    });
                } else if (radioButtonVisitante.isChecked()) {
                    collectionUsers.whereEqualTo("Nombre",prefs.getString("user",null)).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            int saldoActual = Objects.requireNonNull(task.getResult().getDocuments().get(0).getLong("Saldo")).intValue();
                            int nuevoSaldo = saldoActual - seekbar.getProgress();
                            saldoDispDetalle.setText(nuevoSaldo+"");
                            String checkRBVisit=  "Victoria Visitante ("+partidoDTO.getHomeTeam()+" vs "+partidoDTO.getAwayTeam()+")";



                            String documentId = task.getResult().getDocuments().get(0).getId();
                            DocumentReference docRef = collectionUsers.document(documentId);
                            docRef.update("Saldo", nuevoSaldo, "Apuestas", FieldValue.arrayUnion(new HistorialDTO(partidoDTO.getEstado(), checkRBVisit, seekbar.getProgress())))

                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getContext(), "Apuesta realizada correctamente", Toast.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "No se ha podido realizar la apuesta", Toast.LENGTH_LONG).show();

                                    });
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Debe seleccionar un equipo ganador!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
}