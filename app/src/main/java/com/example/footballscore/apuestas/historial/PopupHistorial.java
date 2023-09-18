package com.example.footballscore.apuestas.historial;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.footballscore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopupHistorial extends Dialog implements LifecycleOwner {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    CollectionReference collectionUsers = firebaseFirestore.collection("Usuarios");
    private RecyclerView recyclerHistorial;
    private EnCursoAdapter adapter;
    private LifecycleRegistry lifecycleRegistry;

    public PopupHistorial(@NonNull Activity activity) {
        super(activity);
        lifecycleRegistry = new LifecycleRegistry(this);
        lifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_historial);

        recyclerHistorial = findViewById(R.id.recyclerHistorial);

        getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    dismiss();
                }
            }
        });
        SharedPreferences prefs = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        collectionUsers.whereEqualTo("Nombre", prefs.getString("user", null))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            List<HashMap<String, Object>> apuestas = (List<HashMap<String, Object>>) documentSnapshot.get("Apuestas");

                            if (apuestas != null) {
                                List<HistorialDTO> historialDTOList = new ArrayList<>();
                                for (HashMap<String, Object> apuesta : apuestas) {
                                    String estado = (String) apuesta.get("estado");
                                    String nombre = (String) apuesta.get("nombre");
                                    Long cantidadLong = (Long) apuesta.get("cantidad");
                                    int cantidad = cantidadLong != null ? cantidadLong.intValue() : 0;

                                    HistorialDTO historialDTO = new HistorialDTO(estado, nombre, cantidad);
                                    historialDTOList.add(historialDTO);
                                }

                                recyclerHistorial.setLayoutManager(new LinearLayoutManager(getContext()));
                                adapter = new EnCursoAdapter();
                                recyclerHistorial.setAdapter(adapter);
                                adapter.setItemList(historialDTOList);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Error al obtener las apuestas", Toast.LENGTH_LONG).show();
                    }
                });


        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycleRegistry;
    }
}
