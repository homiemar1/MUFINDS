package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PrincipalActivity extends AppCompatActivity {
    private int condicion = 1;
    private int tema = 1;
    ImageView ivFotoMusicaPrincipal;
    ArrayList<String> al;
    int i = 0;

    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().show();

        database = FirebaseFirestore.getInstance();

        ivFotoMusicaPrincipal = findViewById(R.id.ivFotoMusicaPrincipal);

        al = new ArrayList<>();

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
    }

    public void onClickPersonas (View view) {
        condicion = 2;
    }

    public void onClickDislike(View view) {
        if (i >= al.size()) {
            i=0;
            String enlaze = al.get(i);

            Picasso.with(this).load(Uri.parse(enlaze)).into(ivFotoMusicaPrincipal);

            i += 1;
        }
        else {
            String enlaze = al.get(i);

            Picasso.with(this).load(Uri.parse(enlaze)).into(ivFotoMusicaPrincipal);

            i += 1;
        }

    }

    public void onClickLike(View view) {
        if (i >= al.size()) {
            i=0;
            String enlaze = al.get(i);
            Picasso.with(this).load(Uri.parse(enlaze)).into(ivFotoMusicaPrincipal);

            i += 1;
        }
        else {
            String enlaze = al.get(i);

            Picasso.with(this).load(Uri.parse(enlaze)).into(ivFotoMusicaPrincipal);
            i += 1;
        }

    }


}