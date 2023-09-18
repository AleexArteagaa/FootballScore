package com.example.footballscore.apuestas;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballscore.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PopupAdapter extends RecyclerView.Adapter<PopupAdapter.ViewHolder> {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    CollectionReference collectionUsers = firebaseFirestore.collection("Usuarios");
    private ArrayList<ObjetoApuesta> objetoApuestaArrayList;
    private Context context;
    private TextView saldoD;

    public PopupAdapter(Context context, ArrayList<ObjetoApuesta> objetoApuestaArrayList, TextView saldoDisp) {
        this.context = context;
        this.objetoApuestaArrayList = objetoApuestaArrayList;
        this.saldoD= saldoDisp;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vista_objeto_apuesta, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ObjetoApuesta obj = objetoApuestaArrayList.get(position);
        holder.textMoney.setText(String.valueOf(obj.getPrecio()));
        holder.textObj.setText(obj.getNombreObjeto());
        int imageResId = context.getResources().getIdentifier(obj.getFoto(), "drawable", context.getPackageName());
        holder.objImage.setImageResource(imageResId);

        adquirible(holder, obj);

    }

    private void adquirible(@NonNull ViewHolder holder, ObjetoApuesta obj) {
        if (obj.getPrecio() > Integer.parseInt(saldoD.getText().toString())){
            holder.textDisp.setText("SALDO INSUFICIENTE");
            holder.textDisp.setTextColor(Color.RED);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error de compra")
                            .setMessage("Saldo insuficiente para comprar este artículo.")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .show();
                }
            });
        } else {
            holder.textDisp.setText("CANJEABLE");
            holder.textDisp.setTextColor(Color.GREEN);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Confirmación de compra")
                            .setMessage("¿Deseas comprar este artículo?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences prefs = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                    collectionUsers.whereEqualTo("Nombre",prefs.getString("user",null)).get().addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            int saldoActual = Integer.parseInt(saldoD.getText().toString());
                                            int nuevoSaldo = saldoActual - obj.getPrecio();
                                            saldoD.setText(Integer.parseInt(saldoD.getText().toString())- obj.getPrecio()+"");

                                            String documentId = task.getResult().getDocuments().get(0).getId();
                                            DocumentReference docRef = collectionUsers.document(documentId);
                                            docRef.update("Saldo", nuevoSaldo)
                                                    .addOnSuccessListener(aVoid -> {
                                                        notifyDataSetChanged();
                                                    })
                                                    .addOnFailureListener(e -> {

                                                    });
                                            notifyDataSetChanged();
                                        }
                                    });

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return objetoApuestaArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView moneyImage;
        ImageView objImage;
        TextView textMoney;
        TextView textObj;
        TextView textDisp;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            moneyImage = itemView.findViewById(R.id.moneyImage);
            textMoney = itemView.findViewById(R.id.objMoney);
            objImage = itemView.findViewById(R.id.objImage);
            textObj = itemView.findViewById(R.id.objName);
            textDisp= itemView.findViewById(R.id.textDisponible);
            layout= itemView.findViewById(R.id.contentHolder);
        }
    }
}
