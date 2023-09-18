package com.example.footballscore.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.example.footballscore.favoritos.AdapterFavoritos;
import com.example.footballscore.favoritos.Favoritos;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FavoritosFragment  extends DialogFragment {
    private RecyclerView recyclerVFavoritos;
    private AdapterFavoritos adapterFavoritos;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionUsers = firebaseFirestore.collection("Usuarios");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_popup_favoritos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerVFavoritos = view.findViewById(R.id.recyclerFavoritos);

        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        collectionUsers.whereEqualTo("Nombre", prefs.getString("user", null))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            List<HashMap<String, Object>> apuestas = (List<HashMap<String, Object>>) documentSnapshot.get("Favoritos");

                            if (apuestas != null) {
                                List<Favoritos> favoritosList = new ArrayList<>();
                                for (HashMap<String, Object> fav : apuestas) {
                                    String liga = (String) fav.get("fotoLiga");
                                    String pais = (String) fav.get("fotoPais");
                                    String nameLiga = (String) fav.get("nombreLiga");
                                    Long idLeague= (Long) fav.get("idLeague");

                                    Favoritos favorito = new Favoritos(nameLiga, liga, pais, Math.toIntExact(idLeague));
                                    favoritosList.add(favorito);
                                }

                                recyclerVFavoritos.setLayoutManager(new LinearLayoutManager(requireContext()));
                                adapterFavoritos = new AdapterFavoritos(requireContext(), favoritosList);
                                recyclerVFavoritos.setAdapter(adapterFavoritos);
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error al obtener las apuestas", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
