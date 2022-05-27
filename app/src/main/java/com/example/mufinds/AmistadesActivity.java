package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AmistadesActivity extends AppCompatActivity {
    private FirebaseFirestore database;
    private SharedPreferences sharedPref;
    private TextView tvSolicitudes,tvAmigos;
    private AmigosList amigosList;
    private ListView lvSolicitudesAmistad, lvAmigos;
    private ArrayList<String> usuariosSolicitud, cancionesEnComunSolicitud, fotosPerfilSolicitud;
    private ArrayList<String> usuariosAmistad, instagramAmistad, fotosPerfilAmistad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amistades);
        //añadir

        database = FirebaseFirestore.getInstance();
        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);

        tvSolicitudes = findViewById(R.id.tvSolicitudes);
        tvAmigos = findViewById(R.id.tvAmigos);

        tvSolicitudes.setPaintFlags(tvSolicitudes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvAmigos.setPaintFlags(tvAmigos.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //Solicitudes de amistad
        lvSolicitudesAmistad = findViewById(R.id.lvSolicitudesAmistad);
        onClickLvSolicitudesAmistad();

        //Amigos actuales
        lvAmigos = findViewById(R.id.lvAmigos);
        onClickLvAmigos();

        getInformacionUsuarios();
    }

    private void onClickLvSolicitudesAmistad() {
        lvSolicitudesAmistad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //añadir usuario
                AlertDialog.Builder alert = new AlertDialog.Builder(AmistadesActivity.this);
                alert.setTitle("Añadir amistad");
                alert.setMessage("Estas segur@ de que quieres añadir a este usuario?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                añadirUsuario(position);
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alert.show();
            }
        });
    }

    private void onClickLvAmigos() {
        lvAmigos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //eliminar amigo
                AlertDialog.Builder alert = new AlertDialog.Builder(AmistadesActivity.this);
                alert.setTitle("Eliminar amistad");
                alert.setMessage("Estas segur@ de que quieres eliminar de tus contactos a este usuario? Esta acción será permanente")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                borrarUsuario(position);
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                alert.show();
            }
        });
    }

    private void getInformacionUsuarios() {
        usuariosSolicitud = new ArrayList<>();
        cancionesEnComunSolicitud = new ArrayList<>();
        fotosPerfilSolicitud = new ArrayList<>();
        usuariosAmistad = new ArrayList<>();
        instagramAmistad = new ArrayList<>();
        fotosPerfilAmistad = new ArrayList<>();

        String nombreUsuario = sharedPref.getString("nombreUsuario", "");
        database.collection("relacionUsuarioUsuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(nombreUsuario)){
                            for (Map.Entry<String, Object> entry : document.getData().entrySet()) {
                                if (!nombreUsuario.equals(entry.getKey())) {
                                    if (!(boolean)entry.getValue()) {
                                        usuariosSolicitud.add(entry.getKey());
                                    }
                                    else {
                                        usuariosAmistad.add(entry.getKey());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                    return;
                }
                getInformacionUsuariosAmpliada();
            }
        });
    }

    private void getInformacionUsuariosAmpliada() {
        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (usuariosSolicitud.contains(document.getId())){
                            //cancionesEnComunSolicitud.add((String) document.getData().get("cancionesComun"));
                            cancionesEnComunSolicitud.add("11" + " canciones en comun");
                            fotosPerfilSolicitud.add((String) document.getData().get("fotoPerfil"));
                        }
                        else if (usuariosAmistad.contains(document.getId())) {
                            instagramAmistad.add((String) document.getData().get("instagram"));
                            fotosPerfilAmistad.add((String) document.getData().get("fotoPerfil"));
                        }
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }

                amigosList = new AmigosList(AmistadesActivity.this, usuariosSolicitud, cancionesEnComunSolicitud, fotosPerfilSolicitud);
                lvSolicitudesAmistad.setAdapter(amigosList);
                amigosList = new AmigosList(AmistadesActivity.this, usuariosAmistad, instagramAmistad, fotosPerfilAmistad);
                lvAmigos.setAdapter(amigosList);
            }
        });
    }

    private void borrarUsuario (int position) {
        Map<String,Object> eliminar = new HashMap<>();
        eliminar.put(usuariosAmistad.get(position), FieldValue.delete());
        database.collection("relacionUsuarioUsuario").document(sharedPref.getString("nombreUsuario", "")).update(eliminar);

        eliminar = new HashMap<>();
        eliminar.put(sharedPref.getString("nombreUsuario", ""), FieldValue.delete());
        database.collection("relacionUsuarioUsuario").document(usuariosAmistad.get(position)).update(eliminar);

        usuariosAmistad.remove(position);
        instagramAmistad.remove(position);
        fotosPerfilAmistad.remove(position);

        amigosList.notifyDataSetChanged();
    }

    private void añadirUsuario(int position){
        Map<String,Object> update = new HashMap<>();
        update.put(usuariosSolicitud.get(position), true);
        database.collection("relacionUsuarioUsuario").document(sharedPref.getString("nombreUsuario", ""))
                .update(update);

        update = new HashMap<>();
        update.put(sharedPref.getString("nombreUsuario", ""), true);
        database.collection("relacionUsuarioUsuario").document(usuariosSolicitud.get(position))
                .update(update);

        getInformacionUsuarios();
        amigosList.notifyDataSetChanged();
    }
}