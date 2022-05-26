package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AmistadesActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private FirebaseFirestore database;
    private TextView tvSolicitudes, tvAmigos;
    private AmigosList amigosList;
    private ListView lvSolicitudesAmistad, lvAmigos;
    private ArrayList<String> usersSolicitud, cancionesEnComun, fotosPerfilSolicitud;
    private ArrayList<String> usersAmistad, instagrams, fotosPerfilAmistad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amistades);

        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        database = FirebaseFirestore.getInstance();

        tvSolicitudes = findViewById(R.id.tvSolicitudes);
        tvAmigos = findViewById(R.id.tvAmigos);

        tvSolicitudes.setPaintFlags(tvSolicitudes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvAmigos.setPaintFlags(tvAmigos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        usersSolicitud = new ArrayList<>();
        cancionesEnComun = new ArrayList<>();
        fotosPerfilSolicitud = new ArrayList<>();
        instagrams = new ArrayList<>();
        usersAmistad = new ArrayList<>();
        fotosPerfilAmistad = new ArrayList<>();

        //Solicitudes de amistad
        lvSolicitudesAmistad = findViewById(R.id.lvSolicitudesAmistad);

        lvSolicitudesAmistad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //añadir usuario
                AlertDialog.Builder alert = new AlertDialog.Builder(AmistadesActivity.this);
                alert.setTitle("Añadir amistad");
                alert.setMessage("Estas segur@ de que quieres añadir a este usuario?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

            }
        });

        //Amigos actuales (hace falta cambiar las canciones en comun por una descripcion)
        lvAmigos = findViewById(R.id.lvAmigos);
        lvAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //eliminar amigo
                AlertDialog.Builder alert = new AlertDialog.Builder(AmistadesActivity.this);
                alert.setTitle("Eliminar amistad");
                alert.setMessage("Estas segur@ de que quieres eliminar de tus contactos a este usuario? Esta acción será permanente")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                alert.show();
            }
        });

        getInformacionAmistades();
    }

    private void getInformacionAmistades () {
        String nombreUsuario = sharedPref.getString("nombreUsuario", "");
        database.collection("relacionUsuarioUsuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, Object> informacion = null;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (nombreUsuario.equals(document.getId())) {
                            informacion = document.getData();
                        }
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
                guardarInformacion(informacion);
            }
        });
    }

    private void guardarInformacion(Map<String, Object> informacion) {
        for (Map.Entry entry : informacion.entrySet()) {
            boolean condicion = false;
            if ((boolean)entry.getValue()) {
                usersAmistad.add((String) entry.getKey());
                condicion = buscarInformacionUsuarios((String) entry.getKey(), 0);
            }
            else {
                usersSolicitud.add((String) entry.getKey());
                condicion = buscarInformacionUsuarios((String) entry.getKey(), 1);
            }
        }
        amigosList = new AmigosList(AmistadesActivity.this, usersAmistad, instagrams, fotosPerfilAmistad);
        lvAmigos.setAdapter(amigosList);
        amigosList = new AmigosList(AmistadesActivity.this, usersSolicitud, cancionesEnComun, fotosPerfilSolicitud);
        lvSolicitudesAmistad.setAdapter(amigosList);
    }

    private boolean buscarInformacionUsuarios(String nombreUsuario, int valor) {
        boolean condicion = database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (nombreUsuario.equals(document.getId())) {
                            if (valor == 0) {
                                System.out.println("Valor == 0");
                                instagrams.add((String)document.getData().get("instagram"));
                                usersAmistad.add((String) document.getId());
                                fotosPerfilAmistad.add((String) document.getData().get("fotoPerfil"));
                            }
                            else if (valor == 1) {
                                System.out.println("Valor == 1");
                                usersSolicitud.add((String) document.getId());
                                cancionesEnComun.add((String) document.getData()
                                        .get("21" + " canciones en comun"));
                                fotosPerfilSolicitud.add((String) document.getData().get("fotoPerfil"));
                            }
                        }
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
            }
        }).isComplete();
        return condicion;
    }
}