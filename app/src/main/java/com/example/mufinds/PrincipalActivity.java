package com.example.mufinds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.android.gms.tasks.OnCompleteListener;
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
    private ImageView ivBtEditarPerfil, ivFotoPerfilPrincipal;
    private TextView tvNombreUsuarioPrincipal, tvCancionesComunPrincipal, tvDescripcionPrincipal;
    private ArrayList<String> nombresUsuario;
    private ArrayList<String> idsCanciones;
    private HashMap<String, ArrayList<String>> usuarios, canciones;
    private int contadorMusica = 0;
    private int contadorUsuarios = 0;
    private SharedPreferences sharedPref;
    private String nombreUsuario;
    private CalcularCancionesComun calcularCancionesComun;

    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().show();

        calcularCancionesComun = new CalcularCancionesComun();

        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        database = FirebaseFirestore.getInstance();

        ivFotoPerfilPrincipal = findViewById(R.id.ivFotoPrincipal);
        tvNombreUsuarioPrincipal = findViewById(R.id.tvNombreTituloPrincipal);
        tvCancionesComunPrincipal = findViewById(R.id.tvCancionesComunAlbumPrincipal);
        tvDescripcionPrincipal = findViewById(R.id.tvDescripcionArtistaPrincipal);

        ivBtEditarPerfil = findViewById(R.id.btEditarPerfil);

        nombreUsuario = sharedPref.getString("nombreUsuario","");
        getCancionesUsuario();
        getUsuariosLike();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nombreUsuario = sharedPref.getString("nombreUsuario","");
        condicion = 1;
        ivBtEditarPerfil.setImageResource(R.drawable.logogestionarmusica);
        getCancionesUsuario();
        getUsuariosLike();
    }

    public void getUsuariosLike() {
        ArrayList<String> lista = new ArrayList<>();
        database.collection("relacionUsuarioUsuario").document(nombreUsuario).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = task.getResult().getData();

                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if ((boolean)entry.getValue()) {
                            lista.add(entry.getKey());
                        }
                    }
                    getUsuarios(lista);
                }
            }
        });
    }

    private void getUsuarios(ArrayList<String> lista) {
        nombresUsuario = new ArrayList<>();
        usuarios = new HashMap<>();
        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!document.getId().equals(nombreUsuario) && !lista.contains(document.getId())) {
                            String nombreUsuario2 = document.getId();
                            String cancionesComun = calcularCancionesComun.cancionesEnComun(nombreUsuario, nombreUsuario2) + " canciones en com??n";
                            String descripcion = document.getData().get("descripcion").toString();
                            String fotoPerfil = document.getData().get("fotoPerfil").toString();
                            nombresUsuario.add(nombreUsuario2);
                            ArrayList<String> infoUser = new ArrayList<String>();
                            infoUser.add(cancionesComun);
                            infoUser.add(descripcion);
                            infoUser.add(fotoPerfil);
                            usuarios.put(nombreUsuario2, infoUser);
                        }
                    }
                }
            }
        });
    }

    public void getCancionesUsuario() {
        ArrayList<String> lista= new ArrayList<>();
        DocumentReference docRef = database.collection("relacionUsuarioMusica").document(nombreUsuario);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> map = task.getResult().getData();

                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        lista.add(entry.getKey());
                    }
                    getCanciones(lista);
                }
            }
        });

    }

    public void getCanciones(ArrayList<String> cancionesLike) {
        canciones = new HashMap<>();
        idsCanciones = new ArrayList<>();
        database.collection("canciones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (!cancionesLike.contains(document.getId())) {
                            String idcancion = document.getId();

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
                    }
                    a??adirInformacionMusica();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_configuration:
                Intent intent  = new Intent(this, ConfiguracionActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_logout:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Cerrar Sessi??n");
                alert.setMessage("Estas segur@ de que quieres cerrrar la sesi??n?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert.show();
                        Intent i1 = new Intent(PrincipalActivity.this, IniciActivity.class);
                        startActivity(i1);
                        finish();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }); alert.show();
                return true;

            case R.id.action_informacio:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("INFORMACI??N DE LA APLICACI??N");
                dialog.setMessage("MUFINDS tiene el objetivo de ayudar al usuario a conocer personas con el mismo gusto musical. \n \n"
                        + "Esta app esta desarrollada por Marouan Hammich y Nerea L??pez. \n \n"
                        + "Versi??n 1.0");
                dialog.setPositiveButton(" Aceptar ", new DialogInterface.OnClickListener() {
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
        Intent intent;
        if (condicion == 1) {
            intent = new Intent(PrincipalActivity.this, MusicaActivity.class);
        }
        else {
            intent = new Intent(PrincipalActivity.this, EditarPerfilActivity.class);
        }
        startActivity(intent);

    }

    public void onClickAmistades(View view) {
        Intent intent = new Intent(PrincipalActivity.this, AmistadesActivity.class);
        startActivity(intent);
    }

    public void onClickMusica(View view) {
        condicion = 1;
        ivBtEditarPerfil.setImageResource(R.drawable.logogestionarmusica);
        a??adirInformacionMusica();
    }

    public void onClickPersonas (View view) {
        condicion = 2;
        ivBtEditarPerfil.setImageResource(R.drawable.logoeditarperfilredimensionado);
        a??adirInformacionUsuario();
    }

    public void onClickDislike(View view) {
        if (condicion == 1) {
            if (!idsCanciones.isEmpty()) {
                contadorMusica+=1;
                a??adirInformacionMusica();
            }
        }
        else {
            if (!nombresUsuario.isEmpty()) {
                contadorUsuarios += 1;
                a??adirInformacionUsuario();
            }
        }
    }

    public void onClickLike(View view) {
        String nombreUsuario = sharedPref.getString("nombreUsuario","");
        if (condicion == 1) {
            if (!idsCanciones.isEmpty()) {
                String idcancion = idsCanciones.get(contadorMusica);
                idsCanciones.remove(contadorMusica);
                contadorMusica += 1;
                insertarDatos(idcancion, nombreUsuario);
                a??adirInformacionMusica();
            }
        }
        else {
            if (!nombresUsuario.isEmpty()) {
                String idUsuario = nombresUsuario.get(contadorUsuarios);
                contadorUsuarios += 1;
                insertarDatosUsuario(idUsuario, nombreUsuario);
                a??adirInformacionUsuario();
            }
        }
    }

    private void insertarDatosUsuario(String idUsuario, String nombreUsuario) {
        database.collection("relacionUsuarioUsuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    WriteBatch batch = database.batch();
                    for (DocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(idUsuario)) {
                            DocumentReference docRef = document.getReference();
                            Map<String, Object> new_map = new HashMap<>();
                            new_map.put(nombreUsuario, false);
                            batch.update(docRef, new_map);
                        }
                    }
                    batch.commit();
                }
            }
        });
    }

    private void a??adirInformacionUsuario() {
        if (!nombresUsuario.isEmpty()) {
            if (contadorUsuarios >= nombresUsuario.size()) {
                contadorUsuarios = 0;
                getUsuariosLike();
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
            } else {
                Picasso.with(this).load(Uri.parse(fotPerfil)).noFade().into(ivFotoPerfilPrincipal);
            }
        }
        else {
            tvNombreUsuarioPrincipal.setText("No hay m??s usuarios");
            tvCancionesComunPrincipal.setText("-");
            tvDescripcionPrincipal.setText("-");
            ivFotoPerfilPrincipal.setImageResource(R.drawable.fotoperfil);
        }
    }

    private void a??adirInformacionMusica() {
        if (!idsCanciones.isEmpty()) {
            if (contadorMusica >= idsCanciones.size()) {
                contadorMusica = 0;
                getCancionesUsuario();
            }

            String idCancion = idsCanciones.get(contadorMusica);

            ArrayList<String> array = canciones.get(idCancion);
            String linkPortada = array.get(0);
            String titulo = array.get(1);
            String artista = array.get(2);
            String album = array.get(3);
            String fechaLanzamiento = array.get(4);

            Picasso.with(this).load(Uri.parse(linkPortada)).noFade().into(ivFotoPerfilPrincipal);
            String nombreFecha = titulo + " - " + fechaLanzamiento;
            tvNombreUsuarioPrincipal.setText(nombreFecha);
            tvCancionesComunPrincipal.setText(album);
            tvDescripcionPrincipal.setText(artista);

            System.out.println("CONTADOR A??ADIR INFO FINAL -->" + contadorMusica);

        }
        else {
            tvNombreUsuarioPrincipal.setText("No hay m??s canciones");
            tvCancionesComunPrincipal.setText("-");
            tvDescripcionPrincipal.setText("-");
            ivFotoPerfilPrincipal.setImageResource(R.drawable.fotoperfil);
        }
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
        });
    }


    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar_menu, menu);
        return true;
    }
}