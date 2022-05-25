package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PrincipalActivity extends AppCompatActivity {
    private int condicion = 1;
    private int tema = 1;
    private ImageView ivBtEditarPerfil, ivFotoPerfilPrincipal;
    private TextView tvNombreUsuarioPrincipal, tvCancionesComunPrincipal, tvDescripcionPrincipal;
    private ArrayList<String> nombresUsuario, idsCanciones;
    private HashMap<String, ArrayList<String>> usuarios, canciones;
    private int contadorMusica = 0;
    private int contadorUsuarios = 0;
    private SharedPreferences sharedPref;

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().show();

        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        database = FirebaseFirestore.getInstance();

        ivFotoPerfilPrincipal = findViewById(R.id.ivFotoPrincipal);
        tvNombreUsuarioPrincipal = findViewById(R.id.tvNombreTituloPrincipal);
        tvCancionesComunPrincipal = findViewById(R.id.tvCancionesComunAlbumPrincipal);
        tvDescripcionPrincipal = findViewById(R.id.tvDescripcionArtistaPrincipal);

        ivBtEditarPerfil = findViewById(R.id.btEditarPerfil);

        idsCanciones = new ArrayList<>();
        getCanciones();
        nombresUsuario = new ArrayList<>();
        getUsuarios();
    }

    private void getUsuarios() {
        usuarios = new HashMap<String, ArrayList<String>>();
        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!document.getId().equals(sharedPref.getString("nombreUsuario",""))) {
                            String nombreUsuario = document.getId();
                            String cancionesComun = "11" + " canciones en comun";
                            String descripcion = document.getData().get("descripcion").toString();
                            String fotoPerfil = document.getData().get("fotoPerfil").toString();
                            nombresUsuario.add(nombreUsuario);
                            ArrayList<String> infoUser = new ArrayList<String>();
                            infoUser.add(cancionesComun);
                            infoUser.add(descripcion);
                            infoUser.add(fotoPerfil);
                            usuarios.put(nombreUsuario, infoUser);
                        }

                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
            }
        });
    }

    public void getCanciones() {
        canciones = new HashMap<String, ArrayList<String>>();
        database.collection("canciones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String idcancion = "";
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        idcancion = document.getId();
                        String link = document.getData().get("link").toString();
                        String titulo = document.getData().get("titulo").toString();
                        String artista = document.getData().get("artista").toString();
                        String album = document.getData().get("album").toString();
                        String fechaLanzamiento = Long.toString((Long) document.getData().get("fecha_lanzamiento"));

                        ArrayList<String> infoCancion = new ArrayList<String>();
                        infoCancion.add(link);
                        infoCancion.add(titulo);
                        infoCancion.add(artista);
                        infoCancion.add(album);
                        infoCancion.add(fechaLanzamiento);

                        idsCanciones.add(idcancion);
                        canciones.put(idcancion, infoCancion);
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
                añadirInformacionMusica(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_configuration:
                Intent intent  = new Intent(this, ConfiguracionActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_cambiarTema:
                Toast.makeText(this, "Has seleccionado cambiar tema", Toast.LENGTH_SHORT).show();
                if (tema == 1) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    tema = 2;
                }
                else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    tema = 1;
                }
                return true;

            case R.id.action_logout:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Cerrar Sessión");
                alert.setMessage("Estas segur@ de que quieres cerrrar la sesión?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert.show();
                        Intent i1 = new Intent(PrincipalActivity.this, IniciActivity.class);
                        startActivity(i1);
                        finish();
                    }
                })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }); alert.show();
                return true;

            case R.id.action_informacio:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("INFORMACIÓN DE LA APLICACIÓN");
                dialog.setMessage("MUFINDS tiene el objetivo de ayudar al usuario a conocer personas con el mismo gusto musical. \n \n"
                        + "Esta app esta desarrollada por Marouan Hammich y Nerea López. \n \n"
                        + "Versión 1.0");
                dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
                dialog.show();
                return true;
            case R.id.action_exit:
                finishAffinity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickEditarPerfil(View view) {
        if (condicion == 1) {
            Intent intent = new Intent(PrincipalActivity.this, MusicaActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(PrincipalActivity.this, EditarPerfilActivity.class);
            startActivity(intent);
        }

    }

    public void onClickAmistades(View view) {
        Intent intent = new Intent(PrincipalActivity.this, AmistadesActivity.class);
        startActivity(intent);
    }

    public void onClickMusica(View view) {
        condicion = 1;
        ivBtEditarPerfil.setImageResource(R.drawable.logogestionarmusica);
        añadirInformacionMusica(false);
    }

    public void onClickPersonas (View view) {
        condicion = 2;
        ivBtEditarPerfil.setImageResource(R.drawable.logoeditarperfilredimensionado);
        añadirInformacionUsuario(false);
    }

    public void onClickDislike(View view) {
        if (condicion == 1) {
            añadirInformacionMusica(true);
        }
        else {
            añadirInformacionUsuario(true);
        }
    }

    public void onClickLike(View view) {
        String nombreUsuario = sharedPref.getString("nombreUsuario","");
        if (condicion == 1) {
            String idcancion = idsCanciones.get(contadorMusica);
            insertarDatos(idcancion, nombreUsuario);
            añadirInformacionMusica(true);
        }
        else {
            añadirInformacionUsuario(true);
        }
    }

    private void añadirInformacionUsuario(boolean valor) {
        if (!valor && contadorUsuarios != 0) {
            contadorUsuarios -= 1;
        }
        if (contadorUsuarios >= nombresUsuario.size()) {
            contadorUsuarios = 0;
        }
        String nombreUsuario = nombresUsuario.get(contadorUsuarios);
        ArrayList<String> array = usuarios.get(nombreUsuario);
        String cancionComun = array.get(0);
        String descripcion = array.get(1);
        String fotPerfil = array.get(2);

        tvNombreUsuarioPrincipal.setText(nombreUsuario);
        tvCancionesComunPrincipal.setText(cancionComun);
        tvDescripcionPrincipal.setText(descripcion);

        if ("".equals(fotPerfil) || "R.drawable.fotoperfil".equals(fotPerfil)) {
            ivFotoPerfilPrincipal.setImageResource(R.drawable.fotoperfil);
        }
        else {
            Picasso.with(this).load(Uri.parse(fotPerfil)).into(ivFotoPerfilPrincipal);
        }

        contadorUsuarios += 1;
    }

    private void añadirInformacionMusica(boolean valor) {
        if (!valor && contadorMusica != 0) {
            contadorMusica -= 1;
        }
        if (contadorMusica >= idsCanciones.size()) {
            contadorMusica = 0;
        }
        String idCancion = idsCanciones.get(contadorMusica);
        ArrayList<String> array = canciones.get(idCancion);
        String linkPortada = array.get(0);
        String titulo = array.get(1);
        String artista = array.get(2);
        String album = array.get(3);
        String fechaLanzamiento = array.get(4);

        Picasso.with(this).load(Uri.parse(linkPortada)).into(ivFotoPerfilPrincipal);
        String nombreFecha = titulo + " - " + fechaLanzamiento;
        tvNombreUsuarioPrincipal.setText(nombreFecha);
        tvCancionesComunPrincipal.setText(album);
        tvDescripcionPrincipal.setText(artista);

        contadorMusica += 1;
    }

    public void insertarDatos(String idcancion, String nombreUsuario) {
        database.collection("relacionUsuarioMusica").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    WriteBatch batch = database.batch();

                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(nombreUsuario)) {
                            DocumentReference docRef = document.getReference();
                            Map<String, Object> new_map = new HashMap<>();
                            new_map.put(idcancion, idcancion);
                            batch.update(docRef, new_map);
                        }
                    }
                    batch.commit();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}