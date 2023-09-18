package com.example.footballscore.apuestas;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.text.TextRunShaper;
import android.net.ipsec.ike.TunnelModeChildSessionParams;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class PopupActivity extends Dialog {

    private RecyclerView recyclerView;
    private PopupAdapter adapter;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    CollectionReference collectionUsers = firebaseFirestore.collection("Usuarios");

    public PopupActivity(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_apuestas);

        // Obtén referencias al RecyclerView y al título de la página emergente
        recyclerView = findViewById(R.id.popup_recycler_view);
        ArrayList<ObjetoApuesta> objetoApuestaArrayList = new ArrayList<>();
        objetoApuestaArrayList.add(new ObjetoApuesta("PlayStation 5", 7000, "play5"));
        objetoApuestaArrayList.add(new ObjetoApuesta("Patinete eléctrico", 6000, "patin"));
        objetoApuestaArrayList.add(new ObjetoApuesta("Altavoz JBL 5", 3000, "jbl"));
        objetoApuestaArrayList.add(new ObjetoApuesta("Pelota", 500, "balon"));
        objetoApuestaArrayList.add(new ObjetoApuesta("IPhone 14", 9000, "iphone"));
        objetoApuestaArrayList.add(new ObjetoApuesta("Monitor HP", 5500, "monitor"));
        objetoApuestaArrayList.add(new ObjetoApuesta("Combo teclado-ratón", 1000, "combo"));
        objetoApuestaArrayList.add(new ObjetoApuesta("Viaje a Singapur", 12000, "viaje"));


        // Configura el RecyclerView con un LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crea y configura el adaptador del RecyclerView
        TextView saldoDisp= findViewById(R.id.textMoneyApuestas);
        SharedPreferences prefs = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        collectionUsers.whereEqualTo("Nombre",prefs.getString("user",null)).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int saldoActual = Objects.requireNonNull(task.getResult().getDocuments().get(0).getLong("Saldo")).intValue();
                        saldoDisp.setText(saldoActual+"");
                    }
                });

        adapter = new PopupAdapter(getContext(), objetoApuestaArrayList, saldoDisp);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    }
}
