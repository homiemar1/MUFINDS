package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MusicaActivity extends AppCompatActivity {
    private FirebaseFirestore database;
    private SharedPreferences sharedPref;
    private AmigosList amigosList;
    private ListView lvGestionarMusica;
    private String[] nombreCanciones;
    private String[] artistasCanciones;
    private String[] portadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);

        database = FirebaseFirestore.getInstance();
        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);

        lvGestionarMusica = findViewById(R.id.lvGestionarMusica);

        nombreCanciones = new String[23];
        artistasCanciones = new String[23];
        portadas = new String[23];

        lvGestionarMusica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MusicaActivity.this);
                dialogo1.setTitle("Eliminar canción");
                dialogo1.setMessage("La acción será permanente. ¿Quieres eliminar la canción seleccionada?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {

                    }
                });
                dialogo1.show();
            }
        });

        getInfoCanciones();
    }

    private void getInfoCanciones () {
        String nombreUsuario = sharedPref.getString("nombreUsuario", "");
        String[] idCanciones = new String[50];
        database.collection("relacionUsuarioMusica").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                Map<String, Object> allInfo = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(nombreUsuario)){
                            allInfo = document.getData();
                        }
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
                if (allInfo != null) {
                    System.out.println("All info no esta vacio");
                    int i = 0;
                    for (String idCancion : allInfo.keySet()) {
                        idCanciones[i] = idCancion;
                        i++;
                    }
                    getCanciones(idCanciones);
                }
            }
        });
    }

    private void getCanciones(String [] idCanciones) {
        database.collection("canciones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int i = 0;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(idCanciones[i])){
                            nombreCanciones[i] = document.getData().get("titulo").toString();
                            artistasCanciones[i] = document.getData().get("artista").toString();
                            portadas[i] = document.getData().get("link").toString();
                        }
                        i++;
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
                amigosList = new AmigosList(MusicaActivity.this, nombreCanciones, artistasCanciones, portadas);
                lvGestionarMusica.setAdapter(amigosList);
            }
        });
    }
}