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
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicaActivity extends AppCompatActivity {
    private FirebaseFirestore database;
    private SharedPreferences sharedPref;
    private AmigosList amigosList;
    private ListView lvGestionarMusica;
    private List<String> nombreCanciones = new ArrayList<>();
    private List<String> artistasCanciones = new ArrayList<>();
    private List<String> portadas = new ArrayList<>();
    private List<String> idCancion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);

        database = FirebaseFirestore.getInstance();
        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        String nombreUsuario = sharedPref.getString("nombreUsuario","");

        lvGestionarMusica = findViewById(R.id.lvGestionarMusica);

        idCancion = new ArrayList<>();

        lvGestionarMusica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int posicion, long l) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MusicaActivity.this);
                dialogo1.setTitle("Eliminar canción");
                dialogo1.setMessage("La acción será permanente. ¿Quieres eliminar la canción seleccionada?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        amigosList.removeData(posicion, "relacionUsuarioMusica", nombreUsuario, idCancion.get(posicion));
                        idCancion.remove(posicion);
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
        List<String> idCanciones = new ArrayList<>();;
        String nombreUsuario = sharedPref.getString("nombreUsuario", "");
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
                }
                if (allInfo.isEmpty()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MusicaActivity.this);
                    dialog.setTitle("ATENCIÓN");
                    dialog.setMessage("Aún no has añadido ninguna canción. \nVuelve a la pantalla principal y empieza a añadir canciones.");
                    dialog.setPositiveButton(" Aceptar ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else if(allInfo != null) {
                    idCanciones.addAll(allInfo.keySet());
                    getCanciones(idCanciones);
                }
            }
        });
    }

    private void getCanciones(List<String> idCanciones) {
        nombreCanciones = new ArrayList<>();
        artistasCanciones = new ArrayList<>();
        portadas = new ArrayList<>();
        database.collection("canciones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        for (int x = 0; x<idCanciones.size(); x++) {
                            if (document.getId().equals(idCanciones.get(x))){
                                nombreCanciones.add(document.getData().get("titulo").toString());
                                artistasCanciones.add(document.getData().get("artista").toString());
                                portadas.add(document.getData().get("link").toString());
                                idCancion.add(document.getId());
                            }
                        }
                    }
                }
                amigosList = new AmigosList(MusicaActivity.this, nombreCanciones, artistasCanciones, portadas);
                lvGestionarMusica.setAdapter(amigosList);
            }
        });
    }

}