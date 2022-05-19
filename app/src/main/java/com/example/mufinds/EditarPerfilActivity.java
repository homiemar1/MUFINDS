package com.example.mufinds;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

public class EditarPerfilActivity extends AppCompatActivity {
    private Spinner sp_editar_genero;
    private ImageView ivFotoPerfilEditarPerfil;
    private Uri imageUri;
    EditText etNombreUsuarioEditarPerfil, etDescripcionEditarPerfil;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        sharedPref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        ivFotoPerfilEditarPerfil = findViewById(R.id.ivFotoPerfilEditarPerfil);
        ivFotoPerfilEditarPerfil.setImageResource(R.drawable.fotoperfil);

        etNombreUsuarioEditarPerfil = findViewById(R.id.etNombreUsuarioEditarPerfil);
        etDescripcionEditarPerfil = findViewById(R.id.etDescripcionEditarPerfil);

        sp_editar_genero = findViewById(R.id.spEditarGeneroEditarPerfil);
        String[] datos = new String[] {"Mujer", "Hombre", "Prefiero no contestar", "No binario"};
        sp_editar_genero.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,datos));
        String nombre = sharedPref.getString("nombre","");
        String descripcion = sharedPref.getString("descripcion","");

        etNombreUsuarioEditarPerfil.setText(nombre);
        etDescripcionEditarPerfil.setText(descripcion);

    }

    public void onClickAceptar(View view) {
        String nombre = etNombreUsuarioEditarPerfil.getText().toString();
        String descripcion = etDescripcionEditarPerfil.getText().toString();
        String genero = sp_editar_genero.getSelectedItem().toString();

        if ("".equals(nombre)) {
            etNombreUsuarioEditarPerfil.setError("Introduce un nombre de usuario");
            return;
        }
        else {
            //actualizar los datos
            editor.putString("descripcion",descripcion);
            editor.putString("nombre", nombre);
            editor.putString("genero", genero);
            editor.putString("uriFoto", String.valueOf(imageUri));
            editor.commit();

            database = FirebaseFirestore.getInstance();

        }
    }

    public void onClickFotoDePerfil (View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        someActivityResultLauncher.launch(gallery);
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