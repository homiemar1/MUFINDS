package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ConfiguracionActivity extends AppCompatActivity {
    private Intent intent;
    SharedPreferences sharedPref;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        database = FirebaseFirestore.getInstance();
    }

    public void onClickCambiarNombreUsuario (View view) {
        intent = new Intent(ConfiguracionActivity.this, CambiarUsuariooContraseñaActivity.class);
        intent.putExtra("variable", 1);
        startActivity(intent);
    }

    public void onClickCambiarContraseña (View view) {
        intent = new Intent(ConfiguracionActivity.this, CambiarUsuariooContraseñaActivity.class);
        intent.putExtra("variable", 2);
        startActivity(intent);
    }

    public void onClickEliminarCuenta  (View view) {
        String nombreUsuario = sharedPref.getString("nombreUsuario", "");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Eliminar cuenta");
        alert.setMessage("Estas segur@ de que quieres eliminar tu cuenta? Esta acción será permanente").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(nombreUsuario)) {
                                    database.collection("users").document(nombreUsuario).delete();
                                    database.collection("relacionUsuarioMusica").document(nombreUsuario).delete();
                                    database.collection("relacionUsuarioUsuario").document(nombreUsuario).delete();
                                    eliminarSubRegistros(nombreUsuario);
                                }
                            }
                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                    }
                });
                restartAplicacion();
                //finishAffinity();
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        alert.show();
    }

    private void eliminarSubRegistros(String nombreUsuario) {
        Map<String,Object> updates = new HashMap<>();
        updates.put(nombreUsuario, FieldValue.delete());

        database.collection("relacionUsuarioUsuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        database.collection("relacionUsuarioUsuario").document(document.getId()).update(updates);
                    }
                }
            }
        });
    }

    public void restartAplicacion(){
        Intent mIntent = new Intent(ConfiguracionActivity.this,
                IniciActivity.class);
        finish();
        startActivity(mIntent);
    }
}