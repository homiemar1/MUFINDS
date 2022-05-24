package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class PrincipalActivity extends AppCompatActivity {
    private int condicion = 1;
    private int tema = 1;
    private CardView cvMusicaPrincipal, cvPersonasPrincipal;
    private ImageView ivFotoMusicaPrincipal, ivBtEditarPerfil, ivFotoPerfilPrincipal;
    private TextView tvNombreUsuarioPrincipal, tvCancionesComunPrincipal, tvDescripcionPrincipal;
    private ArrayList<String> al, nombresUsuario;
    private HashMap<String, ArrayList<String>> usuarios;
    private int i = 0;
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

        ivFotoPerfilPrincipal = findViewById(R.id.ivFotoPerfilPrincipal);
        tvNombreUsuarioPrincipal = findViewById(R.id.tvNombreUsuarioPrincipal);
        tvCancionesComunPrincipal = findViewById(R.id.tvCancionesComunPrincipal);
        tvDescripcionPrincipal = findViewById(R.id.tvDescripcionPrincipal);

        cvMusicaPrincipal = findViewById(R.id.cvMusicaPrincipal);
        cvPersonasPrincipal = findViewById(R.id.cvPersonasPrincipal);

        ivFotoMusicaPrincipal = findViewById(R.id.ivFotoMusicaPrincipal);
        ivBtEditarPerfil = findViewById(R.id.btEditarPerfil);

        al = getCanciones();
        nombresUsuario = new ArrayList<>();
        usuarios = getUsuarios();
    }

    private HashMap<String, ArrayList<String>> getUsuarios() {
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
        return usuarios;
    }

    public ArrayList<String> getCanciones() {
        ArrayList<String> al = new ArrayList<>();
        database.collection("canciones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String link = document.getData().get("link").toString();
                        al.add(link);
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
            }
        });

        return al;
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
        cvMusicaPrincipal.setVisibility(View.VISIBLE);
        cvPersonasPrincipal.setVisibility(View.INVISIBLE);
    }

    public void onClickPersonas (View view) {
        condicion = 2;
        ivBtEditarPerfil.setImageResource(R.drawable.logoeditarperfilredimensionado);
        cvMusicaPrincipal.setVisibility(View.INVISIBLE);
        cvPersonasPrincipal.setVisibility(View.VISIBLE);

        añadirInformacionUsuario(false);
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

    public void onClickDislike(View view) {
        if (condicion == 1) {
            if (i >= al.size()) {
                i = 0;
                String enlaze = al.get(i);

                Picasso.with(this).load(Uri.parse(enlaze)).into(ivFotoMusicaPrincipal);

            } else {
                String enlaze = al.get(i);
                Picasso.with(this).load(Uri.parse(enlaze)).into(ivFotoMusicaPrincipal);
            }
            i += 1;
        }
        else {
            añadirInformacionUsuario(true);
        }
    }

    public void onClickLike(View view) {
        String nombreUsuario = sharedPref.getString("nombreUsuario","");
        /*Map<String, String> mapa = new HashMap<>();
        mapa.put("haha", "hehe");
        database.collection("relacion").document(nombreUsuario).set(mapa);*/
        if (condicion == 1) {
            if (i >= al.size()) {
                i=0;
                String enlaze = al.get(i);
                Picasso.with(this).load(Uri.parse(enlaze)).into(ivFotoMusicaPrincipal);

            }
            else {
                String enlaze = al.get(i);

                Picasso.with(this).load(Uri.parse(enlaze)).into(ivFotoMusicaPrincipal);
            }
            i += 1;
        }
        else {
            añadirInformacionUsuario(true);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}