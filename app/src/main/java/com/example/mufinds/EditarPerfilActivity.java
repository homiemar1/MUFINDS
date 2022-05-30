package com.example.mufinds;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditarPerfilActivity extends AppCompatActivity {
    private Spinner sp_editar_genero;
    private ImageView ivFotoPerfilEditarPerfil;
    private Uri imageUri;
    private EditText etNombreEditarPerfil, etApellidoEditarPerfil, etDescripcionEditarPerfil, etInstagramEditar;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private FirebaseFirestore database;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;


    private String nombre2, apellido2, descripcion2,genero2, insta2, fotoPerfil2,fotoPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();

        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        database = FirebaseFirestore.getInstance();

        ivFotoPerfilEditarPerfil = findViewById(R.id.ivFotoPerfilEditarPerfil);
        etInstagramEditar = findViewById(R.id.etInstagramEditar);


        fotoPerfil = sharedPref.getString("idFoto", "R.drawable.fotoperfil");
        if ("R.drawable.fotoperfil".equals(fotoPerfil) || "".equals(fotoPerfil)) {
            ivFotoPerfilEditarPerfil.setImageResource(R.drawable.fotoperfil);
        }
        else {
            Picasso.with(this).load(Uri.parse(fotoPerfil)).noFade().into(ivFotoPerfilEditarPerfil);
        }


        etNombreEditarPerfil = findViewById(R.id.etNombreEditarPerfil);
        etApellidoEditarPerfil = findViewById(R.id.etApellidoEditarPerfil);
        etDescripcionEditarPerfil = findViewById(R.id.etDescripcionEditarPerfil);

        sp_editar_genero = findViewById(R.id.spEditarGeneroEditarPerfil);
        String[] datos = new String[] {"Mujer", "Hombre", "Prefiero no contestar", "No binario"};
        sp_editar_genero.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,datos));
        String nombre = sharedPref.getString("nombre","");
        String apellidos = sharedPref.getString("apellido","");
        String descripcion = sharedPref.getString("descripcion","");
        String instagram = sharedPref.getString("instagram","");
        String genero = sharedPref.getString("genero","");

        etNombreEditarPerfil.setText(nombre);
        etApellidoEditarPerfil.setText(apellidos);
        etDescripcionEditarPerfil.setText(descripcion);
        etInstagramEditar.setText(instagram);
        int posicion = buscarPosicion(datos, genero);
        sp_editar_genero.setSelection(posicion);
    }

    private int buscarPosicion(String[] datos, String genero) {
        int posicion = 0;
        for (String dato : datos) {
            if (genero.equals(dato)) {
                break;
            }
            posicion ++;
        }
        return posicion;
    }

    public void onClickAceptar(View view) {
        nombre2 = etNombreEditarPerfil.getText().toString();
        apellido2 = etApellidoEditarPerfil.getText().toString();
        descripcion2 = etDescripcionEditarPerfil.getText().toString();
        genero2 = sp_editar_genero.getSelectedItem().toString();
        insta2 = etInstagramEditar.getText().toString();
        fotoPerfil2 = "";

        if ("".equals(nombre2)) {
            etNombreEditarPerfil.setError("Introduce un nombre de usuario");
            return;
        }
        if ("".equals(insta2)) {
            etInstagramEditar.setError("Introduce tu Instagram");
            return;
        }

        if (imageUri == null) {
            fotoPerfil2 = "";
            guardarDatos();
        }
        else {
            String nombreUsuario = sharedPref.getString("nombreUsuario","");
            StorageReference sr = FirebaseStorage.getInstance().getReference();
            StorageReference storage = sr.child("fotosperfiles").child(nombreUsuario);
            if ("R.drawable.fotoperfil".equals(fotoPerfil) || "".equals(fotoPerfil)) {
                dialog.show();
                recogerFoto(storage);
            }
            else {
                dialog.show();
                deleteFoto(storage);
            }

        }

    }

    private void deleteFoto(StorageReference storage) {
        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                recogerFoto(storage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    public void onClickFotoDePerfil (View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        someActivityResultLauncher.launch(gallery);
    }

    public void actualizarDatos(String documento, String dato) {
        database.collection("users").document(sharedPref.getString("nombreUsuario", "")).update(documento, dato)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditarPerfilActivity.this, "No se han podido cambiar bien los datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void recogerFoto(StorageReference storage) {
        storage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                descargarFoto(storage);
            }
        }).addOnFailureListener(EditarPerfilActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarPerfilActivity.this, "La imagen no se ha podido subir correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void descargarFoto(StorageReference storage) {
        storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                editor.putString("idFoto", uri.toString());
                String nombreUsuario = sharedPref.getString("nombreUsuario","");
                database.collection("users").document(nombreUsuario)
                        .update("fotoPerfil", uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                guardarDatos();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(EditarPerfilActivity.this, "Foto no guardada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarDatos() {
        editor.putString("descripcion",descripcion2);
        actualizarDatos("descripcion", descripcion2);

        editor.putString("nombre", nombre2);
        actualizarDatos("nombre", nombre2);

        editor.putString("apellido", apellido2);
        actualizarDatos("apellido", apellido2);

        editor.putString("genero", genero2);
        actualizarDatos("genero", genero2);

        editor.putString("instagram", insta2);
        actualizarDatos("instagram", insta2);

        editor.commit();
        dialog.dismiss();
        finish();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        imageUri = intent.getData();
                        ivFotoPerfilEditarPerfil.setImageURI(imageUri);
                    }
                }
            });
}