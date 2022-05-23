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
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RegistroActivity2 extends AppCompatActivity {
    private Intent intent;
    private ImageView ivFotoPerfilRegistro;
    private Uri imageUri;
    EditText etNombreUsuarioRegistro, etDescripcionRegistro, etInstragram;
    Usuario u;
    FirebaseFirestore database;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);

        ivFotoPerfilRegistro = findViewById(R.id.ivFotoPerfilRegistro);
        ivFotoPerfilRegistro.setImageResource(R.drawable.fotoperfil);

        etNombreUsuarioRegistro = findViewById(R.id.etNombreUsuarioRegistro);
        etDescripcionRegistro = findViewById(R.id.etDescripcionRegistro);
        etInstragram = findViewById(R.id.etInstragram);

        u = (Usuario) getIntent().getSerializableExtra("usuario");
        database = FirebaseFirestore.getInstance();

        sharedPref = this.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
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
                        ivFotoPerfilRegistro.setImageURI(imageUri);

                    }
                }
            });

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageUri = data.getData();
        ivFotoPerfilRegistro.setImageURI(imageUri);
    }

    public void onClickAtras (View view) {
        intent = new Intent(RegistroActivity2.this, RegistroActivity.class);
        startActivity(intent);
    }

    public void onClickFinalizar (View view) {
        if (imageUri == null) {
            u.setFotoPerfil("R.drawable.fotoperfil");
        }
        else {
            u.setFotoPerfil(imageUri.toString());
        }


        String nombre_usuario = etNombreUsuarioRegistro.getText().toString();
        String descripcion = etDescripcionRegistro.getText().toString();
        String instagram = etInstragram.getText().toString();

        if ("".equals(nombre_usuario)) {
            etNombreUsuarioRegistro.setError("Introduce tu nombre de usuario");
            return;
        }
        if (" ".equals(nombre_usuario)) {
            etNombreUsuarioRegistro.setError("No se permiten espacios");
            return;
        }
        if ("".equals(instagram)) {
            etInstragram.setError("Introduce tu Instagram");
            return;
        }

        database.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean check = true;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().equals(nombre_usuario)){
                            check = false;
                        }
                    }
                } else {
                    System.out.println("Error getting documents." + task.getException());
                }
                if (check) {
                    u.setNombreUsuari(nombre_usuario);
                    u.setDescripcion(descripcion);
                    u.setInstagram(instagram);

                    database.collection("users").document(u.getNombreUsuari()).set(u);

                    editor.putString("nombre", u.getNombre());
                    editor.putString("apellido", u.getApellido());
                    editor.putString("email", u.getEmail());
                    editor.putString("password", u.getPassword());
                    editor.putString("genero", u.getGenero());
                    editor.putString("descripcion", u.getDescripcion());
                    editor.putString("nombreUsuario", u.getNombreUsuari());
                    editor.putString("idFoto",u.getFotoPerfil());
                    editor.putString("fechaNacimiento", u.getDataNaixement());
                    editor.putString("instagram", u.getInstagram());

                    editor.commit();

                    intent = new Intent(RegistroActivity2.this, PrincipalActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    etNombreUsuarioRegistro.setError("Ese nombre de usuario ya existe");
                }
            }
        });
    }
}