package com.example.mufinds;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity2 extends AppCompatActivity {
    private Intent intent;
    private ImageView ivFotoPerfilRegistro;
    private Uri imageUri;
    EditText etNombreUsuarioRegistro, etDescripcionRegistro;
    Usuario u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro2);

        ivFotoPerfilRegistro = findViewById(R.id.ivFotoPerfilRegistro);
        ivFotoPerfilRegistro.setImageResource(R.drawable.fotoperfil);

        etNombreUsuarioRegistro = findViewById(R.id.etNombreUsuarioRegistro);
        etDescripcionRegistro = findViewById(R.id.etDescripcionRegistro);

        Bundle bundle = this.getIntent().getExtras();
        u = (Usuario) getIntent().getSerializableExtra("usuario");
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
        //comprobar los datos esten bien
        String nombre_usuario = etNombreUsuarioRegistro.getText().toString();
        String descripcion = etDescripcionRegistro.getText().toString();

        if ("".equals(nombre_usuario)) {
            etNombreUsuarioRegistro.setError("Introduce tu nombre de usuario");
        }
        else {
            u.setNombre(nombre_usuario);
            u.setDescripcion(descripcion);

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("project/mufinds-74868/firestore/data");
            DatabaseReference usersRef = ref.child("cancion");
            Map<String, Usuario> users = new HashMap<>();
            users.put(u.getIdUsuario(), u);
            usersRef.setValue(users);

        }

        intent = new Intent(RegistroActivity2.this, PrincipalActivity.class);
        startActivity(intent);
        finish();

    }
}