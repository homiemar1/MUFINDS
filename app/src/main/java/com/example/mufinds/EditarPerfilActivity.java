package com.example.mufinds;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarPerfilActivity extends AppCompatActivity {
    private Spinner sp_editar_genero;
    private ImageView ivFotoPerfilEditarPerfil;
    private Uri imageUri;
    EditText etNombreEditarPerfil, etApellidoEditarPerfil, etDescripcionEditarPerfil;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        database = FirebaseFirestore.getInstance();

        ivFotoPerfilEditarPerfil = findViewById(R.id.ivFotoPerfilEditarPerfil);
        //ivFotoPerfilEditarPerfil.setImageResource(R.drawable.fotoperfil);

        String fotoPerfil = sharedPref.getString("idFoto", "");
        if (!fotoPerfil.equals("")) {
            ivFotoPerfilEditarPerfil.setImageURI(Uri.parse(fotoPerfil));
        }
        else {
            ivFotoPerfilEditarPerfil.setImageResource(R.drawable.fotoperfil);
        }


        etNombreEditarPerfil = findViewById(R.id.etNombreEditarPerfil);
        etApellidoEditarPerfil = findViewById(R.id.etApellidoEditarPerfil);
        etDescripcionEditarPerfil = findViewById(R.id.etDescripcionEditarPerfil);

        sp_editar_genero = findViewById(R.id.spEditarGeneroEditarPerfil);
        String[] datos = new String[] {"Mujer", "Hombre", "Prefiero no contestar", "No binario"};
        sp_editar_genero.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,datos));
        String nombre = sharedPref.getString("nombre","");
        String apellidos = sharedPref.getString("apellido","");
        String descripcion = sharedPref.getString("descripcion","");

        etNombreEditarPerfil.setText(nombre);
        etApellidoEditarPerfil.setText(apellidos);
        etDescripcionEditarPerfil.setText(descripcion);
    }

    public void onClickAceptar(View view) {
        String nombre = etNombreEditarPerfil.getText().toString();
        String apellido = etApellidoEditarPerfil.getText().toString();
        String descripcion = etDescripcionEditarPerfil.getText().toString();
        String genero = sp_editar_genero.getSelectedItem().toString();

        if ("".equals(nombre)) {
            etNombreEditarPerfil.setError("Introduce un nombre de usuario");
            return;
        }
        //actualizar los datos
        editor.putString("descripcion",descripcion);
        actualizarDatos("descripcion", descripcion);

        editor.putString("nombre", nombre);
        actualizarDatos("nombre", nombre);

        editor.putString("apellido", apellido);
        actualizarDatos("apellido", apellido);

        editor.putString("genero", genero);
        actualizarDatos("genero", genero);

        editor.putInt("idFoto", ivFotoPerfilEditarPerfil.getId());
        actualizarDatos("uriFoto", String.valueOf(imageUri));

        editor.commit();
        finish();
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
                        Toast.makeText(EditarPerfilActivity.this, "Update satisfactorio", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditarPerfilActivity.this, "Updatn't satisfactorio", Toast.LENGTH_SHORT).show();
                    }
                });
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